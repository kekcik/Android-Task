package com.android_project.kt.datrackchat.dictionary;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android_project.kt.datrackchat.MainActivity;
import com.android_project.kt.datrackchat.R;
import com.android_project.kt.datrackchat.managers.DictionaryManager;
import com.android_project.kt.datrackchat.models.Word;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//TODO: Словарь
public class DictionaryFragment extends Fragment {
    private static final int R_LAYOUT = R.layout.dictionary_fragment_layout;

    public DictionaryFragment() {
    }

    private View rootView;

    public void restart() {
        if (rootView != null) {
            if (adapter == null) {
                adapter = new WordRecyclerAdapter(getContext(), dictionary);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public static Fragment newInstance() {
        DictionaryFragment fragment = new DictionaryFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    List<Word> dictionary;
    RecyclerView recyclerView;
    WordRecyclerAdapter adapter;
    DictionaryManager manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "started creating");
        rootView = inflater.inflate(R_LAYOUT, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.dict_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (manager == null) manager = new DictionaryManager();
        if (dictionary == null) dictionary = manager.getWholeDictionary(
                (MainActivity) getActivity()
        );
        if (adapter == null) {
            adapter = new WordRecyclerAdapter(getContext(), dictionary);
        }
        recyclerView.setAdapter(adapter);
        EditText editText = (EditText) rootView.findViewById(R.id.search_edit);
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        Log.d(LOG, "Trying get SubDictionary");
                        if (!charSequence.toString().equals("")) {
                            List<Word> subDictionary = manager.getSubDict((MainActivity) getActivity(), charSequence);
                            adapter = new WordRecyclerAdapter(getContext(), subDictionary);
                        } else {
                            adapter = new WordRecyclerAdapter(getContext(), dictionary);
                        }
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                }
        );
        Log.d(LOG, "ended creating");
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    final String LOG = "DictionaryFragment";
}