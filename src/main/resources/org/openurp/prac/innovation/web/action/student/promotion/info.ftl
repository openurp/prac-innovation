<table class="infoTable">
  <tr>
    <td class="title" width="10%">名称</td>
    <td class="content">${project.title}</td>
  </tr>
  <tr>
    <td class="title">简介</td>
    <td class="content">${(project.intro.summaries)!}</td>
  </tr>
  <tr>
    <td class="title">项目类型</td>
    <td class="content">${(project.category.name)!}</td>
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
   <td class="title">项目材料</td>
   <td class="content">
   [#list project.materials as m]
     ${m.stageType.name}：[@b.a href="!attachment?material.id="+m.id target="_blank"]${m.fileName}[/@]<br>
   [/#list]
   </td>
  </tr>
  <tr>
    <td class="title">答辩抽签</td>
    <td class="content">
      [#assign levels=project.levels?sort_by("awardYear")/]
      [#if levels?size > 0]
        [#assign lastLevel=levels?last/]
        <input type="hidden" name="project.id" value="${project.id}"/>
        [#if lastLevel.level == stateLevel]
           <button class="btn btn-sm btn-outline-primary" type="button" onclick="return draw_lot(this.form)">${lastLevel.awardYear}结项 答辩抽签</button>
        [/#if]
      [#else]
        ${(project.level.name)!}
      [/#if]
    </td>
  </tr>
</table>
