<template>
	<div>
		<el-date-picker
                v-model="datevalue"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="position: absolute;top: 25%;left: 53%;height: 50%;"
                popper-class="date-style">
                </el-date-picker>
	</div>
</template>
 
 
<script>
	export default {
		data() {
			return {
				type:false,
				datevalue:''
			};
		},
		watch: {

		},
		methods: {
			butClick(){
				if(this.type){
					this.content = '全屏显示';
                    // 取消全屏
					if(document.exitFullscreen){
						document.exitFullscreen()
					}else if(document.webkitCancelFullScreen){
						document.webkitCancelFullScreen()
					}else if(document.mozCancelFullScreen){
						document.mozCancelFullScreen()
					}else if(document.msExitFulldcreen){
						document.msExitFulldcreen()
					}
					this.type = false
				}else{
					this.content = '取消全屏';
                    // 全屏
					var element = document.documentElement
					if(element.requestFullscreen){
						element.requestFullscreen()
					}else if(element.webkitRequestFullScreen){
						element.webkitRequestFullScreen()
					}else if(element.mozRequestFullScreen){
						element.mozRequestFullScreen()
					}else if(element.msRequestFulldcreen){
						element.msRequestFulldcreen()
					}
					this.type = true
				}
			}
		}
	}
</script>