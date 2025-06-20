[#ftl]
[@b.head/]
[@b.toolbar title="项目信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">代码</td>
    <td class="content">${project.code!}</td>
  </tr>
  <tr>
    <td class="title">名称</td>
    <td class="content">${project.title}</td>
  </tr>
  <tr>
    <td class="title">简介</td>
    <td class="content">${(project.intro.summaries)!}</td>
  </tr>
  <tr>
    <td class="title">负责人</td>
    <td class="content">${(project.manager.std.code)!} ${(project.manager.std.name)!}</td>
  </tr>
  <tr>
    <td class="title">联系电话</td>
    <td class="content">${(project.manager.phone)!}</td>
  </tr>
  <tr>
    <td class="title">联系邮箱</td>
    <td class="content">${(project.manager.email)!}</td>
  </tr>
  <tr>
    <td class="title">项目分工</td>
    <td class="content">${(project.manager.duty)!}</td>
  </tr>
  <tr>
    <td class="title">院系</td>
    <td class="content">${(project.department.name)!}</td>
  </tr>
  <tr>
    <td class="title">项目类型</td>
    <td class="content">${(project.category.name)!}</td>
  </tr>
  <tr>
    <td class="title">项目级别</td>
    <td class="content">${(project.level.name)!}</td>
  </tr>
  <tr>
    <td class="title">项目状态</td>
    <td class="content">${(project.state.name)!}</td>
  </tr>
  <tr>
    <td class="title">开始和拟完成于</td>
    <td class="content">${(project.beginOn?string("yyyy-MM-dd"))!}~${(project.endOn?string("yyyy-MM-dd"))!}</td>
  </tr>
  <tr>
    <td class="title">一级学科</td>
    <td class="content">${(project.discipline.name)!}</td>
  </tr>
  <tr>
    <td class="title">经费</td>
    <td class="content">${project.funds!}</td>
  </tr>
  <tr>
    <td class="title">指导老师</td>
    <td class="content">
    [#list (project.instructors)! as i]${i.name}(${i.code})[#if i_has_next],[/#if][/#list]
    </td>
  </tr>
  <tr>
    <td class="title">其他成员</td>
    <td class="content">
    [#list (project.members)! as m]
        [#if m.id!=(project.manager.id)!0]
        ${m.std.name}(${m.std.code})[#if m_has_next],[/#if]
        [/#if]
    [/#list]
    </td>
  </tr>
  <tr>
    <td class="title">创新点和难点</td>
    <td class="content">${(project.intro.innovations)!}</td>
  </tr>
  <tr>
    <td class="title">预期成果</td>
    <td class="content">${(project.intro.products)!}</td>
  </tr>
  <tr>
   <td class="title">备注</td>
   <td class="content">${(project.remark)!}</td>
  </tr>
  <tr>
   <td class="title">项目材料</td>
   <td class="content">
   [#list project.materials as m]
     ${m.stageType.name}：[@b.a href="project!attachment?material.id="+m.id target="_blank"]${m.fileName}[/@]<br>
   [/#list]
   </td>
  </tr>
</table>

[@b.foot/]
