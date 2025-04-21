<template>
  <div style="height: 20vh;background: rgb(18, 180, 139);width: 100%;">
    <div style="display: flex; justify-content: left; align-items: center; height: 100%;">
      <div  style="margin-left: 210px;" >
        <p style=" font-size: 16px;color: #fff;">有问题？要反馈？</p>
        <p style=" font-size: 16px;color: #fff;">(+86) 8888 8888 8888</p>
      </div>
      <div style="margin-left: 200px;" >
        <p style="color: #fff;">联系我们并且提交反馈，我们会悉心听取您的意见！</p>
      </div>
      <div style="margin-left: 200px;" >
        <button @click="goToFeedback" style="padding: 10px 20px; font-size: 16px; 
        color: rgb(18, 180, 139); border: none; border-radius: 5px;
         cursor: pointer;background: #fff; ">提交反馈</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goToFeedback = () => {
  router.push({name: 'Feedback'})
}
</script>

<style scoped>

</style>