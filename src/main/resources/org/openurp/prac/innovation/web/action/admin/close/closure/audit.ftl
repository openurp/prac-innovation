[#ftl]
[@b.head/]
[@b.toolbar title="审核项目结项信息"]bar.addBack();[/@]
[#assign project=closure.project/]
  [@b.form action=b.rest.save(closure) theme="list"]
    [@b.field  label="编号" value="" ]${project.code!'--'}[/@]
    [@b.field  label="名称"]${project.title!}[/@]
    [@b.field  label="简介"] <div style="margin-left:100px">${project.intro.summaries!}</div>[/@]
    [@b.field  label="项目负责人"]${(project.manager.std.code)} ${(project.manager.std.name)}[/@]

    [@b.field  label="项目类型"]${project.category.name!}[/@]
    [@b.field  label="项目级别"]${project.level.name!}[/@]
    [@b.field  label="一级学科"]${(project.discipline.name)}[/@]
    [@b.field  label="指导老师"][#list (project.instructors)! as i]${i.name}(${i.code})[/#list][/@]
    [@b.field  label="其他成员"][#list (project.members)! as i][#if i.id != project.manager.id]${i.std.name}(${i.std.code})[/#if][#if i_has_next]&nbsp;[/#if][/#list] &nbsp;[/@]
    [@b.field  label="申请免答辩"]${closure.exemptionReason!'--'}[/@]
    [@b.field  label="结项材料"][@b.a href="!attachment?project.id=${closure.project.id}" target="_blank" title="下载结项材料"]${closure.project.closureMaterial.fileName}[/@][/@]
    [#if closure.applyExemptionReply]
    [@b.radios label="是否同意免答辩" name="closure.exemptionConfirmed" value=(closure.exemptionConfirmed)!false items="1:common.yes,0:common.no"/]
    [@b.textfield name="closure.applyRejectComment" label="不同意原因" value="${closure.applyRejectComment!}" required="false" maxlength="100" style="width:200px"/]
    [/#if]
    [@b.formfoot]
      <input type="hidden" name="closure.id" value="${closure.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]
[@b.foot/]
