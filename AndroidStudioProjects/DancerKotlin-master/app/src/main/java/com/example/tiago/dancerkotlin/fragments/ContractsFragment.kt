package com.example.tiago.dancerkotlin.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.tiago.dancerkotlin.R
import com.example.tiago.dancerkotlin.adapters.ContractAdapter
import com.example.tiago.dancerkotlin.adapters.DancerAdapter
import com.example.tiago.dancerkotlin.domain.Event
import com.example.tiago.dancerkotlin.model.MVP
import com.example.tiago.dancerkotlin.presenter.PresenterListContract
import com.example.tiago.dancerkotlin.utils.SpacesItemDecoration
import kotlinx.android.synthetic.main.fragment_contracts.*


/**
 * A simple [Fragment] subclass.
 */
class ContractsFragment : Fragment(), MVP.ViewListContracts {



    private var mEvent: Event? = null;
    var presenter: MVP.PresenterListContracts? = null

    var adapter : ContractAdapter? = null;

    companion object {
        val EXTRA_EVENT = "extra_event"
        fun novaInstancia(event: Event): ContractsFragment {
            val parametros = Bundle()
            parametros.putSerializable(EXTRA_EVENT, event)
            val fragment = ContractsFragment()
            fragment.arguments = parametros
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
        mEvent = arguments?.getSerializable(EXTRA_EVENT) as? Event
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater!!.inflate(R.layout.fragment_contracts, container, false)
        presenter = PresenterListContract()
        presenter?.retrieveContracts(mEvent!!)
        presenter?.setView(this);

        return view;
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoadProgresss(true, "Buscando Dancers");
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {  swipeRefreshLayout?.setRefreshing(false)})
        rv_objects?.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        rv_objects?.addItemDecoration(SpacesItemDecoration(20))
        rv_objects.setHasFixedSize(true);
        adapter = ContractAdapter(presenter?.getContracts()!!, activity);
        rv_objects.adapter = adapter;

    }

    override fun updateList() {
        adapter?.notifyDataSetChanged()
    }

    override fun showLoadProgresss(status: Boolean, message: String) {
        if (status) {
            swipeRefreshLayout?.setRefreshing(true)
        } else {
            swipeRefreshLayout?.setRefreshing(false)
        }
        txt_loading?.setText(message)
    }

}// Required empty public constructor
