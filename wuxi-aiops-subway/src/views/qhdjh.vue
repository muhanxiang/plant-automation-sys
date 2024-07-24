<template>
    <div>
        <el-table :data="gridData" style="color:black;width: 912px;overflow:auto;top: -10px;" :header-cell-style="{background:'#1D8ED2',color:'white'}" height="528">
          <el-table-column  label="远动一平面" width="300" align="center">
            <el-table-column property="date" label="序号" width="100" align="center"></el-table-column>
            <el-table-column property="name1" label="文件名" width="300" align="center"></el-table-column>
          </el-table-column>
          <el-table-column  label="远动二平面" width="300" align="center">
            <el-table-column property="date" label="序号" width="100" align="center"></el-table-column>
            <el-table-column property="name2" label="文件名" width="300" align="center"></el-table-column>
          </el-table-column>
          <el-table-column label=" ">
            <el-table-column property="address" label="检验结果" width="116" align="center"></el-table-column>
          </el-table-column>
        </el-table>
    </div>
</template>

<script>
import axios from 'axios';
 export default {
    data(){
        return{
            string123:'',
            gridData: [],
            ListAll:[]
        }
    },
    mounted () {
        this.getUser()
        this.getFileEqual()
    },
    methods: {
        getUser(){
            return axios.get('http://localhost:8090/scms/getString').then(ret => {
                this.string123=ret.data
            })
        },
        getFileEqual(){
            let count=1;
            return axios.get('http://localhost:8090/scms/getFileEqual').then(res => {
                this.ListAll=res.data.data
                let gridData = []
                let dData = []
                for(let i=0;i<this.ListAll.length;i++){
                    dData=this.ListAll[i].split(' ')
                    console.log(dData)
                    //gridData[i]= {name:res.data.data[i],date:count,address:count}
                    gridData[i]= {name1:dData[0],name2:dData[2],date:count,address:dData[3]}
                    count++
                }
                this.gridData = gridData
            })
        }
   },
 };
</script>
