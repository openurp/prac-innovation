[#ftl]
[@b.head/]
[@b.toolbar title="项目结项答辩抽签"]bar.addBack();[/@]
[@b.messages slash="3"/]
<div class="container-fluid">

[#list members as member]
  [#assign project = member.project/]
  [@b.card class="card-info card-outline"]
    [@b.card_header]
      ${project.title}
    [/@]
    <table class="infoTable">
      <tr>
        <td class="title" width="10%">名称</td>
        <td class="content">${project.title}</td>
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
        <td class="title">指导老师</td>
        <td class="content">
        [#list (project.instructors)! as i]${i.name}(${i.code})[#if i_has_next],[/#if][/#list]
        </td>
      </tr>
      <tr>
       <td class="title">项目材料</td>
       <td class="content">
       [#list project.materials as m]
         ${m.stageType.name}：[@b.a href="!attachment?material.id="+m.id target="_blank"]${m.fileName}[/@]<br>
       [/#list]
       </td>
      </tr>
      <tr>
        <td class="title">答辩分组</td>
        <td class="content">${member.group.name} 第${member.idx}位。答辩时间：${member.group.defenseOn} ${member.group.beginAt}~${member.group.endAt} ${member.group.location!}</td>
      </tr>
    </table>
    [/@]
[/#list]

[#if projects?size==0]
  [#if members?size==0]<p> 没有新的项目需要答辩抽签</p>[/#if]
[#else]
[@b.form name="projectForm" action="!draw"]
  [#list projects as project]
  [@b.card class="card-info card-outline"]
    [@b.card_header]
      ${project.title}
    [/@]
    [#include "info.ftl"/]
  [/@]
  [/#list]
[/@]
[/#if]
<script>
  function draw_lot(form){
    if(confirm("确定要抽签吗？")){
      bg.form.submit(form);
    }else{
      return false;
    }
  }
</script>
</div>
[@b.foot/]
