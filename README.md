<div class="markdown-heading" dir="auto">
  <h1 tabindex="-1" class="heading-element" dir="auto">dnd-12th-6-backend</h1>
  <a href="https://github.com/dnd-side-project/dnd-12th-6-backend">https://github.com/dnd-side-project/dnd-12th-6-backend</a><br><br>
  <a id="user-content-dnd-12th-5-backend" class="anchor" aria-label="Permalink: dnd-12th-6-backend" href="#dnd-12th-5-backend">
    <svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true">
      <path d="m7.775 3.275 1.25-1.25a3.5 3.5 0 1 1 4.95 4.95l-2.5 2.5a3.5 3.5 0 0 1-4.95 0 .751.751 0 0 1 .018-1.042.751.751 0 0 1 1.042-.018 1.998 1.998 0 0 0 2.83 0l2.5-2.5a2.002 2.002 0 0 0-2.83-2.83l-1.25 1.25a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042Zm-4.69 9.64a1.998 1.998 0 0 0 2.83 0l1.25-1.25a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042l-1.25 1.25a3.5 3.5 0 1 1-4.95-4.95l2.5-2.5a3.5 3.5 0 0 1 4.95 0 .751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018 1.998 1.998 0 0 0-2.83 0l-2.5 2.5a1.998 1.998 0 0 0 0 2.83Z">
      </path>
    </svg>
  </a>
</div>




# Invity <img src="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/logo2.png" align=left width=100 height=100>
> Invity는 지인과의 모임 순간들을 쉽게 추억하고 관리하며 공유하는 서비스

<br><br>

## 🌟 Key Features
<h3>📫모임을 더욱 의미있게 만드는 초대장 서비스</h3>
<h5>
: 미닝아웃 문화처럼 크고 작은 시간을 의미 있게 보내고 기록하려는 사람들이 증가하고 있습니다.
Invity는 지인과의 모임 순간들을 쉽게 추억하고 관리하며 공유하는 서비스입니다.
유저 리서치 진행시, 지인과의 모임을 위해 카카오톡으로 의견을 취합 할 때 여러 대화 내용이 쌓이면서
모임에 대한 최종 정보를 찾기 어려운 상황을 발견했습니다.  따라서 이와 같은 문제를 해결하고 모임의 의미와 즐거움을 더할 수 있는 초대장 서비스를 기획했습니다.
</h5>
<h3>🚩Goal : 기능 및 장단기 목표설정</h3>
<pre>
  - 1차 MVP 초대장 작성, 응답, 참석자 관리 기능
    + 초대장 작성 및 커스터마이징 기능 제공 
      : 사용자들이 기본적인 초대장 작성 기능을 활용하고, 누구나 쉽게 커스터마이징하여 초대장을 제작하고 공유할 수 있도록 합니다.
    + 참석 의사 취합 및 소통 문제 해결
      : 참석자들이 참석 여부(참석, 불가능, 미정)를 쉽게 응답할 수 있는 기능을 제공하여 참석 의사를 효과적으로 취합하고 소통 문제를 해결합니다.
  - 피드백 기반 기능 개선 및 정식 출시 준비
    + MVP를 통해 얻은 사용자 피드백을 바탕으로 기능을 개선하여, DND 이후 정식 출시를 목표로 합니다.
</pre>
<h3>📌Solution : 초대장 서비스 방향성 및 핵심 기능</h3>
<pre>
1. 사용자 중심의 간편한 초대장 작성 및 관리<br>
  - 초대장 작성부터 참석 여부 취합, 공유까지 직관적인 UX를 제공하여 사용자 편의성을 극대화한다.<br>
  - 누구나 손쉽게 초대장을 만들고 공유할 수 있도록 간결한 UI와 플로우를 설계한다.<br>
  <br>
2. 초대 관리의 최적화로 원활한 모임 진행 지원<br>
  - 참석자의 응답 여부와 전달사항을 쉽게 취합할 수 있는 구조를 마련하여 모임 준비의 혼선을 줄인다.<br>
  - 초대장 링크를 통해 모임의 정보 사항을 명확하게 공지함으로써 전달의 효율성을 높인다.<br>
  <br>
3. 특별한 모임 경험 설계<br>
  - 테마를 가진 모임 주최자가 해당 모임을 대표하는 이미지를 생성할 수 있도록 하여 모임의 만족도를 높인다.<br>
  - 테마와 가치가 담긴 초대장을 기록할 수 있는 기능을 제공해 소모임의 관계를 더욱 강화하고 지속적인 유대감을 형성한다.<br>
</pre>
<img src="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/serviceIntroduce2.png" width="800" alt="서비스 소개">
<img src="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/IntroduceService.png" width="800" alt="서비스 소개">


<br><br>

## 🛠️ Tech Stack(백엔드)
<img src="https://img.shields.io/badge/java%2017-262261?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=for-the-badge&logo=Spring JPA&logoColor=white"><br>
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON Web Tokens&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"><br>
<img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/postgresql-4169E1?style=flat-square&logo=postgresql&logoColor=white"/>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=for-the-badge&logo=Amazon S3&logoColor=white"><br>
<img src="https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=for-the-badge&logo=GitHub Actions&logoColor=white">

<br>

## 🖼️ System Architecture
<img width="650" height = "330" alt="시스템 아키텍쳐 이미지" src="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/system_architecture.png"/>

<br>

##  ERD
<a href="https://www.erdcloud.com/d/rWmcJqj8KJshkowt9">
    <img alt="ERD" width="900" height="500" src="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/ERD1.png" />
</a>

<br>
<br>
<br>


<!-- 데모 영상---------------------------------------------------------------------------------- -->
<h2 tabindex="-1" dir="auto"><a id="user-content--데모-영상" class="anchor" aria-hidden="true" tabindex="-1" href="#-데모-영상"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path d="m7.775 3.275 1.25-1.25a3.5 3.5 0 1 1 4.95 4.95l-2.5 2.5a3.5 3.5 0 0 1-4.95 0 .751.751 0 0 1 .018-1.042.751.751 0 0 1 1.042-.018 1.998 1.998 0 0 0 2.83 0l2.5-2.5a2.002 2.002 0 0 0-2.83-2.83l-1.25 1.25a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042Zm-4.69 9.64a1.998 1.998 0 0 0 2.83 0l1.25-1.25a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042l-1.25 1.25a3.5 3.5 0 1 1-4.95-4.95l2.5-2.5a3.5 3.5 0 0 1 4.95 0 .751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018 1.998 1.998 0 0 0-2.83 0l-2.5 2.5a1.998 1.998 0 0 0 0 2.83Z"></path></svg></a>🎥 프로토타입데모 영상</h2>

<table>
  <thead>
  <tr>
    <th align="center">메인 및 생성</th>
  </tr>
  </thead>
  <tbody>
    <tr>
    <td align="center"><animated-image data-catalyst=""><a target="_blank" rel="noopener noreferrer" href="" data-target="animated-image.originalLink"><img src="https://github.com/user-attachments/assets/d288b5b5-7fa6-4ad3-8dde-ca6011c99cb8" alt="초대장 생성" style="max: 3000; display: inline-block;" data-target="animated-image.originalImage"></a>
          <span class="AnimatedImagePlayer" data-target="animated-image.player" hidden="">
            <a data-target="animated-image.replacedLink" class="AnimatedImagePlayer-images" href="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/%ED%94%84%EB%A1%9C%ED%86%A0%ED%83%80%EC%9E%851.gif" target="_blank">
    </tr>
  </tbody>
</table>

<table>
  <thead>
  <tr>
    <th align="center">초대장 보기</th>
  </tr>
  </thead>
  <tbody>
    <tr>
    <td align="center"><animated-image data-catalyst=""><a target="_blank" rel="noopener noreferrer" href="" data-target="animated-image.originalLink"><img src="https://github.com/user-attachments/assets/95d98bb0-1a85-4fa9-bbd2-201816a40f6b" alt="초대장 생성" style="max: 3000; display: inline-block;" data-target="animated-image.originalImage"></a>
          <span class="AnimatedImagePlayer" data-target="animated-image.player" hidden="">
            <a data-target="animated-image.replacedLink" class="AnimatedImagePlayer-images" href="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/%ED%94%84%EB%A1%9C%ED%86%A0%ED%83%80%EC%9E%852.gif" target="_blank">
    </tr>
  </tbody>
</table>

<br><br>



<!-- 시연 영상---------------------------------------------------------------------------------- -->
<h2 tabindex="-1" dir="auto"><a id="user-content--시연-영상" class="anchor" aria-hidden="true" tabindex="-1" href="#-데모-영상"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path d="m7.775 3.275 1.25-1.25a3.5 3.5 0 1 1 4.95 4.95l-2.5 2.5a3.5 3.5 0 0 1-4.95 0 .751.751 0 0 1 .018-1.042.751.751 0 0 1 1.042-.018 1.998 1.998 0 0 0 2.83 0l2.5-2.5a2.002 2.002 0 0 0-2.83-2.83l-1.25 1.25a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042Zm-4.69 9.64a1.998 1.998 0 0 0 2.83 0l1.25-1.25a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042l-1.25 1.25a3.5 3.5 0 1 1-4.95-4.95l2.5-2.5a3.5 3.5 0 0 1 4.95 0 .751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018 1.998 1.998 0 0 0-2.83 0l-2.5 2.5a1.998 1.998 0 0 0 0 2.83Z"></path></svg></a>🎥 시연 영상</h2>

<table>
  <thead>
  <tr>
    <th align="center">Invity</th>
  </tr>
  </thead>
  <tbody>
    <tr>
    <td align="center"><animated-image data-catalyst=""><a target="_blank" rel="noopener noreferrer" href="" data-target="animated-image.originalLink"><img src="https://github.com/user-attachments/assets/ba57bf70-7e2e-4efa-9ded-c4f30e138402" alt="초대장 생성" style="max: 3000; display: inline-block;" data-target="animated-image.originalImage"></a>
          <span class="AnimatedImagePlayer" data-target="animated-image.player" hidden="">
            <a data-target="animated-image.replacedLink" class="AnimatedImagePlayer-images" href="https://github.com/seonghuncode/meetingInvitation/blob/master/assets/DND_1%EC%B0%A8_%EA%B2%B0%EA%B3%BC%EB%AC%BC_GIF.gif" target="_blank">
    </tr>
  </tbody>
</table>

<br><br>

<!-- 개발기간 -->
<h2 tabindex="-1" dir="auto"><a id="user-content--개발-기간" class="anchor" aria-hidden="true" tabindex="-1" href="#-개발-기간"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path d="m7.775 3.275 1.25-1.25a3.5 3.5 0 1 1 4.95 4.95l-2.5 2.5a3.5 3.5 0 0 1-4.95 0 .751.751 0 0 1 .018-1.042.751.751 0 0 1 1.042-.018 1.998 1.998 0 0 0 2.83 0l2.5-2.5a2.002 2.002 0 0 0-2.83-2.83l-1.25 1.25a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042Zm-4.69 9.64a1.998 1.998 0 0 0 2.83 0l1.25-1.25a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042l-1.25 1.25a3.5 3.5 0 1 1-4.95-4.95l2.5-2.5a3.5 3.5 0 0 1 4.95 0 .751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018 1.998 1.998 0 0 0-2.83 0l-2.5 2.5a1.998 1.998 0 0 0 0 2.83Z"></path></svg></a>📅 개발 기간</h2>
<p dir="auto">25.01.01. ~ 25.02.28.</p>

<br><br>

<!-- 블로그 ---------------------------------------------------------------------------------- -->
<h2 tabindex="-1" dir="auto"><a id="user-content--데모-영상" class="anchor" aria-hidden="true" tabindex="-1" href="#-데모-영상"><svg class="octicon octicon-link" viewBox="0 0 16 16" version="1.1" width="16" height="16" aria-hidden="true"><path d="m7.775 3.275 1.25-1.25a3.5 3.5 0 1 1 4.95 4.95l-2.5 2.5a3.5 3.5 0 0 1-4.95 0 .751.751 0 0 1 .018-1.042.751.751 0 0 1 1.042-.018 1.998 1.998 0 0 0 2.83 0l2.5-2.5a2.002 2.002 0 0 0-2.83-2.83l-1.25 1.25a.751.751 0 0 1-1.042-.018.751.751 0 0 1-.018-1.042Zm-4.69 9.64a1.998 1.998 0 0 0 2.83 0l1.25-1.25a.751.751 0 0 1 1.042.018.751.751 0 0 1 .018 1.042l-1.25 1.25a3.5 3.5 0 1 1-4.95-4.95l2.5-2.5a3.5 3.5 0 0 1 4.95 0 .751.751 0 0 1-.018 1.042.751.751 0 0 1-1.042.018 1.998 1.998 0 0 0-2.83 0l-2.5 2.5a1.998 1.998 0 0 0 0 2.83Z"></path></svg></a>♾️상세 내역(개인 블로그)</h2>
📌 <a href="https://ysh2954.tistory.com/entry/DND12%EA%B8%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-1%EC%A3%BC%EC%B0%A8">DND동아리 참여 동기 및 아이디어 도출</a><br>
📌 <a href="https://ysh2954.tistory.com/entry/DND12%EA%B8%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-2%EC%A3%BC%EC%B0%A8">필드리서치 및 설계</a><br>
📌 <a href="https://ysh2954.tistory.com/entry/DND12%EA%B8%B0-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-34%EC%A3%BC%EC%B0%A8">사용할 기술 정리</a><br>

<br><br><br><br><br><br>
<br><br><br><br><br><br>













