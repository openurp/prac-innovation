[#ftl]
[@b.head/]
[@b.grid items=projects var="project"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    bar.addItem("${b.text("action.export")}",action.exportData("",null,'fileName=项目信息&template=project.xls'));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="11%" property="code" title="编号"/]
    [@b.col property="title" title="名称"]
          <span style="font-size:0.8em">
           [@b.a href="!info?id=${project.id}"]${project.title}[/@]
          </span>
    [/@]
    [@b.col width="8%" property="manager.std.name" title="负责人"]
      [#if project.members?size>1]
      <span title="拟参加${project.members?size}人">${(project.manager.std.name)!}<sup>${project.members?size}</sup></span>
      [#else]
      ${(project.manager.std.name)!}
      [/#if]
    [/@]
    [@b.col width="7%" property="level.name" title="立项级别"/]
    [@b.col width="8%" property="department.name" title="学院"][#if project.department.shortName??]${project.department.shortName}[#else]${project.department.name}[/#if][/@]
    [@b.col width="7%" title="指导老师"][#list project.instructors as t]${t.name}[#sep],[/#sep][/#list][/@]
    [@b.col width="9%" property="discipline.name" title="学科"/]
    [@b.col width="10%" property="state.name" title="状态"/]
  [/@]
  [/@]
[@b.foot/]
