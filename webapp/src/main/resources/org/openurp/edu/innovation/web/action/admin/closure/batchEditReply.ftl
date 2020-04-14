[#ftl]
[@b.head/]
[@b.toolbar title="批量设置答辩成绩"]
  bar.addItem("保存","saveReply()");
  function saveReply(){
    bg.form.submit(document.closureListForm);
  }
[/@]
[@b.form action="!saveBatchEditReply"  name="closureListForm"]
    [@b.grid items=closures?sort_by(["project","code"]) var="closure"]
      [@b.row]
        [@b.col width="11%" property="project.code" title="编号"/]
        [@b.col width="40%" property="project.title" title="名称"][@b.a href="!attachment?project.id=${closure.project.id}" target="_blank" title="下载结项材料"]${closure.project.title}[/@][/@]
        [@b.col width="8%" property="project.manager.std.user.name" title="负责人"]
          ${(closure.project.manager.std.user.name)!}
        [/@]
        [@b.col width="8%" property="department.name" title="学院"][#if closure.project.department.shortName??]${closure.project.department.shortName}[#else]${closure.project.department.name}[/#if][/@]
        [@b.col width="7%" title="指导老师"][#list closure.project.instructors as t]${t.user.name}[#sep],[/#sep][/#list][/@]
        [@b.col width="9%" property="project.discipline.name" title="学科"/]
        [@b.col width="12%" property="replyScore" title="答辩成绩"]
         <input type="hidden" name="closure.id" value="${closure.id}"/>
         <input type="text" name="closure_${closure.id}.replyScore" style="width:70px"  tabIndex="${closure_index+1}" value="${(closure.replyScore)!}"/>
        [/@]
      [/@]
    [/@]
[/@]

[@b.foot/]
