[#ftl]
[@b.head/]
<style>
  fieldset.listset li > label.title {
    min-width: 10rem;
  }
</style>
[@b.toolbar title="新建/修改项目信息"]bar.addBack();[/@]
  [@b.form action="!save" theme="list"  enctype="multipart/form-data" onsubmit="checkProject"]
    [@b.field label="项目类型"]
        <input name="project.category.id" value="${(project.category.id)!}" type="hidden"/>
        ${project.category.name}
    [/@]
    [@b.textfield name="project.title" label="名称" value="${project.title!}" required="true" maxlength="100" style="width:400px"/]
    [@b.textarea name="intro.summaries" label="简介" value=(project.intro.summaries)! required="true" rows="8" cols="100" maxlength="200" comment="200字以内"/]
    [@b.textfield label="项目负责人" name="managerCode" required="true" value=(managerCode)! readOnly="true"/]
    [@b.textfield label="负责人联系电话" name="manager.phone" required="true" value=(project.manager.phone)!/]
    [@b.textfield label="负责人联系邮箱" name="manager.email" required="true" value=(project.manager.email)! style="width:150px"/]
    [@b.textfield label="负责人项目分工" name="manager.duty" required="true" maxlength="100" value=(project.manager.duty)! style="width:400px"/]
    [@b.startend label="开始和拟完成于"
      name="project.beginOn,project.endOn" required="true,true" start=(project.beginOn)! end=(project.endOn)! format="date"/]

    [@b.select name="project.discipline.id" label="一级学科" value="${(project.discipline.id)!}" option=r"${item.code} ${item.name}"
               style="width:200px;" items=disciplines empty="..." required="true"/]
    [@base.teacher label="指导老师" multiple="true" name="instructorId" style="width:400px;" required="true" values=project.instructors! project = student.project/]

    [@b.field label="其他成员"]
      <select id="studentId" multiple="true" name="studentId" style="width:400px;">
        [#list (project.members)! as m]
        [#if m.id!=(project.manager.id)!0]
        <option value='${m.std.id}' selected>${m.std.name}(${m.std.code})</option>
        [/#if]
        [/#list]
      </select>
    [/@]
    [@b.textarea name="intro.innovations" label="创新点和难点" value=(project.intro.innovations)! required="true" rows="5" cols="100" maxlength="200" comment="100字以内"/]
    [@b.textarea name="intro.products" label="预期成果" value=(project.intro.products)! required="true" rows="5" cols="100" maxlength="200" comment="100字以内"/]
    [@b.field  label="立项材料"]<input type="file" name="attachment"/>
      [#list project.materials as m][#if m.stageType==initialStage.stageType][@b.a target="_blank" href="!attachment?material.id="+m.id]${m.fileName}[/@][/#if][/#list]
    [/@]
    [@b.textfield name="project.remark" label="备注" value="${project.remark!}"  maxlength="100"/]
    [@b.formfoot]
      [#if project.manager??]
      <input type="hidden" name="manager.id" value="${project.manager.id}"/>
      [/#if]
      [#if project.intro??]
      <input type="hidden" name="intro.id" value="${project.intro.id}"/>
      [/#if]
      <input type="hidden" name="project.batch.id" value="${project.batch.id}"/>
      [#if project.persisted]
      <input type="hidden" name="project.id" value="${project.id}"/>
      [/#if]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]

<script>
  beangle.load(["chosen","bui-ajaxchosen"],function(){
    jQuery("#instructorId").ajaxchosen(
      {
          method: 'GET',
          url:  "${b.url('!teacher')}?q={term}"
      }
      , function (data) {
          var items = [];
          var dataObj = eval("(" + data + ")");
          jQuery.each(dataObj.teachers, function (i, teacher) {
               items.push({"value":teacher.id,"text":teacher.name + "(" + teacher.code + ")"});
          });
          return items;
      },
      {width:"400px"}
    );

      jQuery("#studentId").ajaxchosen({ method: 'GET', url:  "${b.url('!student')}?q={term}"}, function (data) {
          var items = [];
          var dataObj = eval("(" + data + ")");
          jQuery.each(dataObj.students, function (i, student) {
              items.push({"value":student.id,"text":student.name + "(" + student.code + ")"});
          });
          return items;
      },
      {width:"400px"}
    );
  });

   function checkProject(form){
    if("" == form['attachment'].value){
      alert("请上传立项材料");
      return false;
    }
    return true;
  }
</script>
[@b.foot/]
