[#ftl]
[@b.head/]
[@b.toolbar title="专家评审信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">项目代码</td>
    <td class="content">${review.project.code!}</td>
  </tr>
  <tr>
    <td class="title">项目名称</td>
    <td class="content">${review.project.title}</td>
  </tr>
  <tr>
    <td class="title">负责人</td>
    <td class="content">${(review.project.manager.std.user.code)!} ${(review.project.manager.std.user.name)!}</td>
  </tr>
  <tr>
    <td class="title">评审成绩</td>
    <td class="content">${(review.score)!}</td>
  </tr>
  <tr>
    <td class="title">建议级别</td>
    <td class="content">${(review.level.name)!}</td>
  </tr>
  <tr>
    <td class="title">评审意见</td>
    <td class="content">${(review.comments)!}</td>
  </tr>
  <tr>
   <td class="title">评审时间</td>
   <td class="content">[#if review.score??]${(review.updatedAt?string('yyyy-MM-dd HH:mm'))!}[/#if]</td>
  </tr>
</table>

[@b.foot/]
