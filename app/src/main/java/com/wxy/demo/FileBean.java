package com.wxy.demo;


import com.wxy.demo.tree.TreeNodeId;
import com.wxy.demo.tree.TreeNodeLabel;
import com.wxy.demo.tree.TreeNodePid;

/**
 * Created by wangxy on 2017/3/6.
 */

public class FileBean {
    @TreeNodeId
    private int _id;
    @TreeNodePid
    private int parentId;
    @TreeNodeLabel
    public String name;
    private long length;
    private String desc;

    public FileBean(int _id, int parentId, String name)
    {
        super();
        this._id = _id;
        this.parentId = parentId;
        this.name = name;
    }
}
