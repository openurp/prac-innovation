[#ftl]
[@b.head/]
[@b.toolbar title="专家立项评审信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">项目代码</td>
    <td class="content">${detail.review.project.code!}</td>
  </tr>
  <tr>
    <td class="title">项目名称</td>
    <td class="content">${detail.review.project.title}</td>
  </tr>
  <tr>
    <td class="title">负责人</td>
    <td class="content">${(detail.review.project.manager.std.code)!} ${(detail.review.project.manager.std.name)!}</td>
  </tr>
  <tr>
    <td class="title">评审成绩</td>
    <td class="content">${(detail.score)!}</td>
  </tr>
  <tr>
    <td class="title">是否推荐立项</td>
    <td class="content">[#if detail.passed??]${detail.passed?string("推荐","不推荐")}[#else]--[/#if]</td>
  </tr>
  <tr>
    <td class="title">评审意见</td>
    <td class="content">${(detail.comments)!}</td>
  </tr>
  <tr>
   <td class="title">评审时间</td>
   <td class="content">[#if detail.score??]${(detail.updatedAt?string('yyyy-MM-dd HH:mm'))!}[/#if]</td>
  </tr>
</table>

[@b.foot/]
