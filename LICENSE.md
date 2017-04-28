 根据个人需求更改Status Bean字段 
 算法部分：
 
 
 private void lift(){
        ArrayList<FileBean> newLists = new ArrayList<>();
        //json中最外层集合
        if(mDepartmentList!=null&&mDepartmentList.size()!=0){
             /*
             分树 走
         */
            
            int size1 = mDepartmentList.size();
            int a = 1;
            for(int i = 0; i<size1;i++){
                int demTag = a;
                DataInfo.DataBean departmentBean = mDepartmentList.get(i);
                //第三个参数是展示时,显示的string
                FileBean newBean = new FileBean(a,0,departmentBean.getTname());
                newLists.add(newBean);
                //我们将额外的值  赋值到一个新的bean中
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
