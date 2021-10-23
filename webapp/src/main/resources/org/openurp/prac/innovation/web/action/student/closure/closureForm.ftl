[#ftl]
[@b.head/]
[#list closures as c]
  [#assign closure = c/]
[/#list]

[@b.toolbar title="项目结项信息"]bar.addBack();[/@]
  [@b.form action="!saveClosure" theme="list" name="closureForm" onsubmit="checkClosure"  enctype="multipart/form-data"]
    [@b.field  label="编号" value="" ]${project.code!'--'}[/@]
    [@b.field  label="名称"]${project.title!}[/@]
    [@b.field  label="简介"] <div style="margin-left:100px">${project.intro.summary!}</div>[/@]
    [@b.field  label="项目负责人"]${(project.manager.std.user.code)} ${(project.manager.std.user.name)}[/@]

    [@b.field  label="项目类型"]${project.category.name!}[/@]
    [@b.field  label="项目级别"]${project.level.name!}[/@]
    [@b.field  label="一级学科"]${(project.discipline.name)}[/@]
    [@b.field  label="指导老师"][#list (project.instructors)! as i]${i.user.name}(${i.user.code})[/#list]&nbsp;[/@]
    [@b.field  label="其他成员"]
      [#if project.members?size>1]
        [#list (project.members)! as i][#if i.id != project.manager.id]${i.std.user.name}(${i.std.user.code})[/#if][#if i_has_next]&nbsp;[/#if][/#list]
      [#else]
        --
      [/#if]
    [/@]
    [#if (closure.exemptionConfirmed)!false]
      [@b.field  label="申请免答辩"]${closure.exemptionReason!} <span class="badge">审核通过</span>[/@]
      [@b.field  label="结项材料"][#list project.materials as m][#if m.stageType==closureStage]${m.fileName}[/#if][/#list][/@]
    [#else]

      [#assign applyStage=project.batch.getStage(applyExemptionReplyStage)/]
      [#if applyStage?? && applyStage.intime]
       [@b.radios label="申请免答辩" id="applyExemptionReply" name="closure.applyExemptionReply" value=(closure.applyExemptionReply)!false items="1:common.yes,0:common.no"/]
       [@b.textfield label="免答辩理由" name="closure.exemptionReason" style="width:400px" id="exemptionReason" value=(closure.exemptionReason)!/]
      [#else]
       [@b.field  label="申请免答辩"]
         <input name="closure.applyExemptionReply" value="0" type="radio" checked="checked"/>
         [#if (closure.applyExemptionReply)!false]
           ${(closure.exemptionReason)!}
           <span class="badge">
           [#if (closure.exemptionConfirmed)??]
            ${closure.exemptionConfirmed?string('审核通过','审核不通过')} [#if !closure.exemptionConfirmed]:${closure.applyRejectComment!}[/#if]
           [#else]未审核
           [/#if]
           </span>
         [#else]不申请[/#if]
       [/@]
      [/#if]
      [@b.field  label="结项材料"]<input type="file" name="attachment"/>
        [#list project.materials as m][#if m.stageType==closureStage][@b.a target="_blank" href="!attachment?material.id="+m.id]${m.fileName}[/@][/#if][/#list]
      [/@]
    [/#if]
    [@b.formfoot]
      [#if closure??]
      <input type="hidden" name="closure.id" value="${closure.id}"/>
      [/#if]
      <input type="hidden" name="project.id" value="${project.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]

[#if !(closure.exemptionConfirmed)!false]
<script>
  jQuery(document).ready(function(){
     jQuery('#applyExemptionReply_0').click(displayReason);
     jQuery('#applyExemptionReply_1').click(displayReason);
     var applyExemption=$("input[name='closure.applyExemptionReply']:checked").val();
     if(applyExemption == '1'){
       jQuery('#exemptionReason').parent().css("display","")
     }else{
       jQuery('#exemptionReason').parent().css("display","none")
     }
  });
  function displayReason(e){
    if(this.value=='1'){
      jQuery('#exemptionReason').parent().css("display","")
    }else{
      jQuery('#exemptionReason').parent().css("display","none")
    }
  }

  function checkClosure(form){
    var applyExemption=$("input[name='closure.applyExemptionReply']:checked").val();

    if(!applyExemption){
       alert("请选择是否申请免答辩!");
       return false;
    }

    if('1'==applyExemption){
      if(form['closure.exemptionReason'].value ==""){
         alert("请填写免答辩理由");
         return false;
      }
    }

    if("" == form['attachment'].value){
      alert("请上传结项材料");
      return false;
    }
    return true;
  }
</script>
[/#if]
[@b.foot/]
