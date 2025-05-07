<script setup></script>

<template>
  <hr class="text-browny mt-10" />
  <div class="flex items-center justify-center px-20 py-10 mx-30">
    <!-- 메뉴 -->
    <div class="flex flex-col w-full">
      <!-- 고객센터 -->
      <div>
        <h1>고객센터</h1>
        <div class="flex gap-5 mt-3">
          <router-link to="/letter" class="flex items-center justify-center text-sm h-8 w-35 ring-1 ring-browny rounded-sm">상담 / 문의</router-link>
          <router-link to="/faq" class="flex items-center justify-center text-sm h-8 w-35 ring-1 ring-browny rounded-sm">자주묻는질문(FAQ)</router-link>
        </div>
        <p class="mt-3 text-sm text-zinc-600">평일 10:00 ~ 18:00 (점심시간 12:00 ~ 13:30) / 휴무: 토요일, 일요일, 공휴일</p>
        <div class="flex mt-1 text-sm">
          <p class="text-zinc-600">전화문의 043-849-1752 | 이메일문의</p>
          <p class="ml-2 text-yellow-900">dla5420@naver.com</p>
        </div>
      </div>
      <!-- 메뉴 -->
      <div class="mt-10">
        <div class="flex gap-10 text-zinc-600">
          <router-link to="/login">로그인/가입</router-link>
          <router-link to="/store">상품구매</router-link>
          <router-link to="/resell">중고장터</router-link>
          <router-link to="/policy">이용약관</router-link>
          <router-link to="/privacy">개인정보처리방침</router-link>
        </div>
      </div>
      <div class="mt-5 flex flex-col flex text-browny text-sm">
        <p>상호명: (주)KitGet | 팀장: 임의순 | 팀원: 박동수 오준석 | 작품제작번호: 제2025-교통대학교-0001호</p>
        <p>주소: 충청북도 충주시 교통대학교 미래융합정보관, 3층 302호 | 업무 및 제휴 dla5420@naver.com | 010-3940-7145</p>
        <p>호스팅 서비스 제공자: Github / Koyeb</p>
        <p>© <strong class="text-zinc-500">Team. KitGet</strong> ALL Rights Reserved</p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
