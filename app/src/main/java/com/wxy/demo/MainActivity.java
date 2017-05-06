package com.wxy.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wxy.demo.tree.Node;
import com.wxy.demo.tree.TreeListViewAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<DataInfo.DataBean> mDepartmentList;
    private HashMap<Integer, Status> mStatusMap;
    private ArrayList<FileBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listV);
        findViewById(R.id.listV);
        initData();
        String  ss = "1.新增电影原声音乐，可以听曲子了&2.开映前 1 小时，打开 App 自动显示电影票，取票更快速&3.部分影院支持购买情侣座，将会开通更多&4.部分影院支持低价爆米花，订单页来购买&5.电影票增加保存到相册，分享给朋友的功能";
//        String dd = "ddd&aaa";
        String[] length  =ss.split("&");
        Log.e("dd",length.length+"");

    }

    private void initData() {
        Gson gson = new Gson();
        DataInfo dataInfo = gson.fromJson(new DemoJson().json, DataInfo.class);
        mDepartmentList = dataInfo.getData();
        mStatusMap = new HashMap<>();
        mDatas = new ArrayList<>();
        lift();
    }
    private void lift(){
        ArrayList<FileBean> newLists = new ArrayList<>();
        if(mDepartmentList!=null&&mDepartmentList.size()!=0){
             /*
             分树 走
         */
            //有几个部门
            int size1 = mDepartmentList.size();
            int a = 1;
            for(int i = 0; i<size1;i++){
                int demTag = a;
                DataInfo.DataBean departmentBean = mDepartmentList.get(i);
                FileBean newBean = new FileBean(a,0,departmentBean.getTname());
                newLists.add(newBean);
                Status status = new Status();
                status.isCheck = false;
                mStatusMap.put(a,status);
                a++;
                Log.e("sssssss",departmentBean.getTname());
                //有多少个 团队
                for(int j = 0;j<departmentBean.getChild().size();j++){
                    int teamTag = a;
                    DataInfo.DataBean.ChildBean teamBean = departmentBean.getChild().get(j);
                    FileBean newBean2 = new FileBean(a,demTag,teamBean.getTname());
                    newLists.add(newBean2);
                    Status status2 = new Status();
                    status2.isCheck = false;
                    status2.departmentId = demTag;
                    mStatusMap.put(a,status2);
                    a++;
                    Log.e("sssssss",teamBean.getTname());
                    for(int k = 0;k<teamBean.getApp().size();k++){
                        DataInfo.DataBean.ChildBean.AppBean staffBean = teamBean.getApp().get(k);
                        FileBean newBean3 = new FileBean(a,teamTag,staffBean.getAname());
                        newLists.add(newBean3);
                        Status status3 = new Status();
                        status3.name  = staffBean.getAname();
                        status3.face  = staffBean.getImg();
                        status3.isCheck = staffBean.getIs_checked().equals("1")?true:false;
                        status3.departmentId = demTag;
                        status3.teamId = teamTag;
                        mStatusMap.put(a,status3);
                        if(staffBean.getIs_checked().equals("1")){
                            Status status1 = mStatusMap.get(demTag);
                            status1.isCheck =true;
                            Status status4 = mStatusMap.get(teamTag);
                            status4.isCheck =true;
                        }
                        a++;
                    }
                }
            }
            mDatas.addAll(newLists);
            SimpleTreeAdapter<FileBean> fileBeanSimpleTreeAdapter = null;
            try {
                fileBeanSimpleTreeAdapter = new SimpleTreeAdapter<>(mListView,this,mDatas,10);
                mListView.setAdapter(fileBeanSimpleTreeAdapter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    获取所有选中的条目  包含 1 2 级
    如果想去除  可以通过 teamId != -1 来区分
     */
    private List<Status> getSelectData(){
        ArrayList<Status> statuses = new ArrayList<>();
        for(int i = 1 ;i<mStatusMap.size();i++){
            Status status = mStatusMap.get(i);
            if(status.isCheck){
                statuses.add(status);
            }
        }
        return statuses;
    }

    private  HashMap<Integer, Boolean> isSelected = new HashMap<>();
    class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

        public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                                 int defaultExpandLevel) throws IllegalArgumentException,
                IllegalAccessException {
            super(mTree, context, datas, defaultExpandLevel);
        }

        @Override
        public View getConvertView(final Node node, final int position, View convertView, ViewGroup parent) {
            int level = node.getLevel();
            final int id = node.getId();
            final Status status = mStatusMap.get(id);
            if (level == 0) {
                convertView = mInflater.inflate(R.layout.item_colleague_one, parent, false);
                TextView nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                checkBox.setChecked(status.isCheck);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        status.isCheck =isChecked;
                        for(int i=  1 ; i <=mStatusMap.size();i++){
                            Status status3 = mStatusMap.get(i);
                            if(status3.departmentId == id)
                                status3.isCheck = isChecked;
                        }
                        notifyDataSetChanged();
                    }
                });
                nameTv.setText((CharSequence) node.getName());
            }else if(level==1){
                convertView = mInflater.inflate(R.layout.item_colleague_two, parent, false);
                TextView nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                checkBox.setChecked(status.isCheck);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        status.isCheck =isChecked;
                        for(int i=  1 ; i <=mStatusMap.size();i++){
                            Status status1 = mStatusMap.get(i);
                            if(status1.teamId==id){
                                status1.isCheck = isChecked;
                            }
                        }
                        if(isChecked){
                            mStatusMap.get(status.departmentId).isCheck = true;
                        }else{
                            //判断1级下面是否 还有选中
                            Status status1 = mStatusMap.get(status.departmentId);
                            boolean flag = false;
                            for(int i=  1 ; i <=mStatusMap.size();i++){
                                Status status3 = mStatusMap.get(i);
                                if(status3.departmentId==status.departmentId&&status3.isCheck&&status3.teamId==-1){
                                    flag = true;
                                }
                            }
                            status1.isCheck = flag;
                        }
                        notifyDataSetChanged();
                    }
                });
                nameTv.setText((CharSequence) node.getName());

            }else if (level == 2) {

                convertView = mInflater.inflate(R.layout.item_colleague_three, parent, false);
                TextView nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                ImageView iconImg = (ImageView) convertView.findViewById(R.id.leftImg);
                final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                checkBox.setChecked(status.isCheck);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //将自己选中
                        status.isCheck =isChecked;
                        //将所有父级选中
                        if(isChecked){
                            Status status2 = mStatusMap.get(status.departmentId);
                            status2.isCheck =true;
                            Status status3 = mStatusMap.get(status.teamId);
                            status3.isCheck =true;
                        }else{

                            Status status2 = mStatusMap.get(status.teamId);
                            status2.isCheck =false;
                            //判断2级下面 是否 还有选中
                            for(int i=  1 ; i <=mStatusMap.size();i++){
                                Status status3 = mStatusMap.get(i);
                                if(status3.teamId==status.teamId&&status3.isCheck){
                                   status2.isCheck =true;
                                }
                            }
                            //判断1级下面是否 还有选中
                            Status status1 = mStatusMap.get(status.departmentId);
                            boolean flag = false;
                            for(int i=  1 ; i <=mStatusMap.size();i++){
                                Status status3 = mStatusMap.get(i);
                                if(status3.departmentId==status.departmentId&&status3.isCheck&&status3.teamId==-1){
                                    flag = true;
                                }
                            }
                            status1.isCheck = flag;
                        }

                        notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,getSelectData().size()+"",Toast.LENGTH_SHORT).show();

                    }
                });
                nameTv.setText((CharSequence) node.getName());
            }
            return convertView;
        }
    }
    public class Status implements Serializable {
        public String name ;
        public boolean isCheck = false;
        public String face;
        public int departmentId = -1;
        public int teamId = -1;
    }
}
