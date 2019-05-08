package com.ranpeak.ProjectX.activity.lobby.forAuthorizedUsers.navigationFragment.profileNavFragment.myResumeFragment.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ranpeak.ProjectX.R;
import com.ranpeak.ProjectX.activity.interfaces.Activity;
import com.ranpeak.ProjectX.activity.lobby.forAuthorizedUsers.navigationFragment.profileNavFragment.myResumeFragment.adapter.MyResumeListAdapter;
import com.ranpeak.ProjectX.activity.lobby.forAuthorizedUsers.navigationFragment.profileNavFragment.myResumeFragment.resume.MyResumeEditActivity;
import com.ranpeak.ProjectX.activity.lobby.forAuthorizedUsers.navigationFragment.profileNavFragment.myResumeFragment.resume.MyResumeViewActivity;
import com.ranpeak.ProjectX.dto.ResumeDTO;
import com.ranpeak.ProjectX.dto.TaskDTO;
import com.ranpeak.ProjectX.dto.pojo.ResumePOJO;
import com.ranpeak.ProjectX.networking.retrofit.ApiService;
import com.ranpeak.ProjectX.networking.retrofit.RetrofitClient;
import com.ranpeak.ProjectX.settings.SharedPrefManager;
import com.ranpeak.ProjectX.viewModel.ResumeViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyResumeFragment extends Fragment implements Activity {

    private View view;
    private RecyclerView recyclerView;
    private MyResumeListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ResumeViewModel resumeViewModel;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);


    public MyResumeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_resume, container, false);
        findViewById();
        onListener();
        initItems();
        return view;
    }

    @Override
    public void findViewById() {
        recyclerView = view.findViewById(R.id.fragment_my_resume_recyclerView);
    }

    @Override
    public void onListener() {

    }

    private void initItems() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MyResumeListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        resumeViewModel = ViewModelProviders.of(this)
                .get(ResumeViewModel.class);
        disposable.add(resumeViewModel.getAllUsersResumes(
                String.valueOf(SharedPrefManager.getInstance(getContext()).getUserLogin()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resumeDTOS -> {
                    adapter.submitList(resumeDTOS);
                })
        );
//        disposable.dispose();

        // if one of the items was clicked
        adapter.setOnItemClickListener(new MyResumeListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(ResumeDTO resume) {
                Intent intent = new Intent(getContext(), MyResumeViewActivity.class);
                intent.putExtra("MyResume", resume);

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(ResumeDTO resumeDTO) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void itemDeletable(boolean delete) {

            }

            @Override
            public void onUpdateStatusClick(ResumeDTO resumeDTO, int pos) {
                if (resumeDTO.getStatus().equals(getString(R.string.not_active))) {
                    resumeDTO.setStatus(getString(R.string.active));
                } else {
                    resumeDTO.setStatus(getString(R.string.not_active));
                }
                resumeViewModel.update(resumeDTO);
                adapter.notifyItemChanged(pos);
            }

            @Override
            public void onDeleteClick(ResumeDTO resumeDTO) {
                resumeViewModel.delete(resumeDTO);

//                apiService.deleteResume(resumeDTO.getId());
            }

            @Override
            public void onEditClick(ResumeDTO resumeDTO) {
                Intent intent = new Intent(getActivity(), MyResumeEditActivity.class);
                intent.putExtra("MyResume", resumeDTO);

                startActivity(intent);
            }
        });
    }

}
