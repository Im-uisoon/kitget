<script setup>
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue'
</script>

<template>
  <Header />
  <section class="w-full flex justify-center mt-10">
    <div class="flex flex-col text-center">
      <p class="text-gray-800 text-3xl font-bold">이용약관</p>
      <p class="text-gray-800 text-xl font-bold">(사이트 감상동의 포함)</p>
    </div>
  </section>

  <!-- 글귀 -->
  <section class="w-full mt-10 px-30">
    <h1 class="h1t">제 1창 총칙</h1>
    <h2 class="h2t">제 1조(목적)</h2>
    <h3 class="h3t">
      이 약관은 KitGet(이하 "발표조"라 한다)가 제공하는 킷겟 서비스(이하 "서비스"라 한다)를 이용함에 있어 회원의 책임사항을 규정함을 목적으로 합니다. <br />
      인터넷, 정보통신망, 모바일 및 스마트 장치 등을 이용하는 전자상거래에 대하여도 그 성질에 반하지 않는 한 이 약관을 준용합니다
    </h3>
    <h2 class="h2t">제 2조 (서비스의 중단)</h2>
    <h3 class="h3t">
      1. 코드의 보수점검, 교체 및 고장, 통신의 두절 등 사유가 발생한 경우 중단 될 수 있습니다<br />
      2. 팀은 프로젝트가 끝난 뒤 사이트 폐지를 결정할 권리가 있습니다<br />
      3. 팀은 본 조에 따른 서비스의 중단에 대하여 어떠한 책임도 부담하지 않습니다
    </h3>
    <h2 class="h2t">제 3조 (회원 탈퇴 및 자격 변경 등)</h2>
    <h3 class="h3t">
      1. 교수님은 운영진을 통해 교수 권한을 부여 받을 수 있습니다. <br />
      가. 교수 권한으로 상품을 등록할 수 있습니다 <br />
      나. 교수 권한으로 상품을 삭제할 수 있습니다 <br />
      다. 교수 권한으로 상품 등록을 건의 할 수 있습니다 <br />
      2. 회원은 팀에게 언제든지 탈퇴를 요청할 수 있으며, 즉시 회원탈퇴를 처리합니다. 단, 회원으로서 사용한 서비스, 게시물은 보유할 수 있습니다. <br />
      3. 비정상적인 행위를 할 경우, 팀은 회원자격을 박탈 및 정지할 수 있습니다 <br />
      가. 가입신청시에 허위 내용을 등록한 경우 또는 타인의 정보를 도용한경우 <br />
      나. 사이트를 목적 외 비정상적으로 사용하거나 접근한 경우 <br />
      다. 사이트의 주 목적을 벗어나 비정상적인 행위를 한 경우 <br />
      4. 각 호의 내용을 바탕으로 비정상적인 행위를 시도한 사용자를 상대로 손해배상청구 및 소송을 진행할 수 있습니다
    </h3>
    <h2 class="h2t">제 4조 (재판권 및 준거법)</h2>
    <h3 class="h3t">
      1. 이 약관의 해석 및 팀괴 회원간의 분쟁에 대하여는 대한민국의 법률을 적용합니다. <br />
      이용약관 공고 및 시행 날자 : 2025년 3월 22일
    </h3>
  </section>

  <Footer />
</template>

<style scoped>
@import 'tailwindcss';

.h1t {
  @apply text-2xl;
}
.h2t {
  @apply text-xl mt-5 font-bold;
}
.h3t {
  @apply text-sm mt-5;
}
</style>
