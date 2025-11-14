<script>
export default {
	onLaunch: function () {
		// 注释掉自动检查登录，让首页自己处理
		// const token = uni.getStorageSync('token');
		// if (!token) {
		// 	uni.reLaunch({
		// 		url: '/pages/login/login'
		// 	});
		// }
	},
	onShow: function () {
		console.log('App Show');
	},
	onHide: function () {
		console.log('App Hide');
	}
}
</script>

<style>
/* 全局样式 */
page {
	font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif;
	font-size: 28rpx;
	color: #333;
	background-color: #f8f8f8;
	line-height: 1.5;
	box-sizing: border-box;
}

/* 去除按钮默认样式 */
button {
	margin: 0;
	padding: 0;
	background-color: transparent;
	border: none;
	outline: none;
	line-height: normal;
}

button::after {
	border: none;
}

/* 去除滚动条 */
::-webkit-scrollbar {
	width: 0;
	height: 0;
	color: transparent;
}

/* 图标字体 */
@font-face {
	font-family: "iconfont";
	src: url('data:font/woff2;charset=utf-8;base64,d09GMgABAAAAAANMAAsAAAAAB1gAAAL+AAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHFQGYACCcAqCIIIKATYCJAMICwYABCAFhGcHNRvEBciemjwJUUEp2c5A8fC/9e17M3P/LrNJNEkmkWQymcWbRBqJkCDRKJ1O6IR3/7+17z4zX5RQPvwrCgkVyYrwKlmRUJvQCrUJtQk1QvyaXgKEAGG2Pd9cFppKk1B5mqL5P8dMl88Dy2+zuaSMbRrHAxxNoKOoiyIrkIA3jF3gEq4TqDWoILZntS8MkcJYFYiLnh4YIlVKKbJmUShsWZrEA6yK0j31BXAffD/+QkQkFHJmrD18MdMHqV/ozyuIaf+nOp0I+vFsYE8jYxlIxEWr7YRoES8jrZXzWjuAoiL1C/15Bf//IXnlL48QCkaRNbATG5KL+IUWIfELt0vCr1QjZZlqA9wCO8CTtLQEMbK0XY9Z91IDdnvmhw+dDx+6Hz92P3rU8/ix5/79jnv3Ou/e7bhzp/3OnfY7d9qK0srbt1sePTJXVBBLS4lqwwYtTc3mTRs2bdq4adPGzZs3bdmyaevWTdu2bdq+fdOOHZt27ty0a9em3bs379mzee/ezfv2bd6/f/OBA5sPHtx86NDmw4c3Hzmy+ehRZPz48ZGTJ0+eOHHi+PHjx44dO3r06JEjR1hHzg5/c/jIkSPHjx/HOpJKHrpz5/adO7dv375169bNmzdv3Lhx/fr1a9euXb169cqVK5cvX7506dLFixcvXLhw/vz5c+fOnT179syZM6dPnz516tTJkydPnDhx/PjxY8eOHTlyBEhG/t9B/X9kQAb+/8PTCvDzdAoAn9Mv4DP8f1/xN/xqfqp/dUUAfK5fCXxe+VO8KspK1d+yAshfBQpkRQprRUlEtcGoNWsNvdYCrNVqZnc9JCJaohbkJgwIjcYga3SMROwylEazDJXGGlRabWOtNofYps6YBGKRGMHWPRA6vUDW6wWJeJ9Qmg2ESrcIanUGhm3rjMVCZ5Q5LCQj9gT1C5bNJ1k0iRn7D7Q8joRSyRR+oJvYMXHYKX6gJJaYULfCNhcEyzSCXnGCZctBbcw816Yd9Qs2n0/AxRxMiTlYkBjCPIF0F1hmvsiKza/vP6DJxSORlJRl5AfkRthhxMIOZR9QJdnECiqt4GbOIWBRGgJ6CieQWcUC1Qyz8tTMrCn9eRXgc7QVOZTIkaMYjR8ZtxSLXJVVtFi0AAAA') format('woff2');
}

.iconfont {
	font-family: "iconfont" !important;
	font-size: 16px;
	font-style: normal;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
}

.icon-search:before {
	content: "\e65c";
}

/* 仅 H5 端生效 */
/* #ifdef H5 */
html,
body,
#app {
  height: 100%;
  overflow: hidden;
}
/* #endif */
</style>