[#ftl]
[@b.head/]
[@b.toolbar title="评审专家账户信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${expert.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">账户</td>
    <td class="content">${expert.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">介绍</td>
    <td class="content" >${expert.intro}</td>
  </tr>
  <tr>
    <td class="title" width="20%">学科</td>
    <td class="content" >${(expert.discipline.name)!}</td>
  </tr>
</table>

[@b.foot/]
