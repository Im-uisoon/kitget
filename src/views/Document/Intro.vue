<script setup>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <Header />

  <!-- 글귀 -->
  <section class="w-full flex justify-center text-yellow-800 mt-10">
    <div class="flex flex-col gap-10 text-center mb-20">
      <h1 class="flex justify-center text-5xl pt-10">
        <p class="text-gray-800 text-5xl font-bold">Kit</p>
        <p class="text-browny text-5xl font-bold">Get</p>
        <i class="fa-solid fa-gear text-3xl text-gray-800 ml-2" />
      </h1>
      <p class="text-xl">KitGet은 이렇게 만들어졌습니다.</p>
      <div>
        살면서 처음으로 작품을 만들어야하는 수업이 있었습니다.
        <br />모든게 서툴고 납땜을 처음 했을 때의 그 기억은 잊을 수 없죠 <br />수 많은 부품이 타들어가고 망가지고, 재사용이 불가능했습니다 <br />새로운 부품을 받아오거나 구매해야하는 경우도 다반사였고 <br />기억력이 좋지 않아 깜빡하고 재료를 구매안한 경우도 있었습니다. <br /><br />KitGet은 그러한 불편함을 최소화 하고, 오로지 학습에만 집중하도록 돕고싶었습니다. <br />다 사용하고 쓸 일 없는 부품은 다음 사용자에게 판매해 서로에게 금전적 여유도 챙겼으면 합니다. <br /><br /><br /><br /><i>좋은 결과 있기를 바랍니다. KitGet 개발자 일동</i>
      </div>
    </div>
  </section>

  <Footer />
</template>

<style scoped></style>
