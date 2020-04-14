[#ftl]
[@b.head/]
[@b.toolbar title="教学日历方案信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${batch.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${batch.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">每周开始时间</td>
    <td class="content" >${batch.firstWeekday}</td>
  </tr>
  <tr>
    <td class="title" width="20%">生效时间</td>
    <td class="content" >${batch.beginOn!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">失效时间</td>
    <td class="content" >${batch.endOn!}</td>
  </tr>
</table>

[@b.foot/]
