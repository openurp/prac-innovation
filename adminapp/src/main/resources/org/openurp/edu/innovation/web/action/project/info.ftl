[#ftl]
[@b.head/]
[@b.toolbar title="课程信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${project.code}</td>
  </tr>
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${project.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">英文名</td>
    <td class="content">${project.enName!}</td>
  </tr>
  [#if project.levels ??]
  <tr>
    <td class="title" width="20%">培养层次</td>
    <td class="content">
      [#list project.levels as level]
        ${level.name}
        [#if level_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  [/#if]
  <tr>
    <td class="title" width="20%">课程种类代码</td>
    <td class="content">${(project.category.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">学分</td>
    <td class="content">${project.credits!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">学时</td>
    <td class="content">${project.creditHours!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">周课时</td>
    <td class="content">${project.weekHours!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">周数</td>
    <td class="content">${project.weeks!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">院系</td>
    <td class="content">${(project.department.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">建议课程类别</td>
    <td class="content">${(project.projectType.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">考试方式</td>
    <td class="content">${(project.examMode.name)!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">成绩记录方式</td>
    <td class="content">${(project.markStyle.name)!}</td>
  </tr>
  <tr>
   <td class="title" width="20%">是否计算绩点</td>
   <td class="content">${(project.calgp?string("是","否"))!}</td>
  </tr>
  [#if project.majors ??]
  <tr>
    <td class="title" width="20%">针对专业</td>
    <td class="content">
      [#list project.majors as major]
        ${major.name}
        [#if major_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  [/#if]
  [#if project.xmajors ??]
  <tr>
    <td class="title" width="20%">排除专业</td>
    <td class="content">
      [#list project.xmajors as major]
        ${major.name}
        [#if major_has_next],[/#if]
      [/#list]
    </td>
  </tr>
  [/#if]
  <tr>
    <td class="title" width="20%">生效时间</td>
    <td class="content" >${project.beginOn!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">失效时间</td>
    <td class="content" >${project.endOn!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content">${project.remark!}</td>
  </tr>
</table>

[@b.foot/]
