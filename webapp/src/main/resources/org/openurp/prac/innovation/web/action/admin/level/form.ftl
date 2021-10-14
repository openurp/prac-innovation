[#ftl]
[@b.head/]
[@b.toolbar title="新建/修改项目信息"]bar.addBack();[/@]
  [@b.form action=b.rest.save(project) theme="list"]
    [@b.textfield name="project.code" label="编号" value="${project.code!}" required="false" maxlength="20"/]
    [@b.textfield name="project.title" label="名称" value="${project.title!}" required="true" maxlength="100" style="width:400px"/]
    [@b.textarea name="intro.summary" label="简介" value=(project.intro.summary)! required="true" rows="8" cols="100" maxlength="700" comment="200字以内"/]
    [@b.textfield label="项目负责人" name="managerCode" required="true" value=(project.manager.std.user.code)! comment=(project.manager.std.user.name)!/]
    [@b.textfield label="联系电话" name="manager.phone" required="true" value=(project.manager.phone)!/]
    [@b.textfield label="联系邮箱" name="manager.email" required="true" value=(project.manager.email)! style="width:150px"/]
    [@b.textfield label="项目分工" name="manager.duty" required="true" maxlength="100" value=(project.manager.duty)! style="width:400px"/]

    [@b.select name="project.department.id" label="院系" value="${(project.department.id)!}" style="width:200px;" items=departments empty="..." required="true"/]
    [@b.select name="project.category.id" label="项目类型" value="${(project.category.id)!}" style="width:200px;" items=projectCategories empty="..." required="true"/]
    [@b.select name="project.level.id" label="项目级别" value="${(project.level.id)!}" style="width:200px;" items=projectLevels empty="..." required="true"/]
    [@b.select name="project.state.id" label="项目状态" value="${(project.state.id)!}" style="width:200px;" items=projectStates empty="..." required="true"/]
    [@b.startend label="开始和拟完成于"
      name="project.beginOn,project.endOn" required="true,true" start=(project.beginOn)! end=(project.endOn)! format="date"/]

    [@b.select name="project.discipline.id" label="一级学科" value="${(project.discipline.id)!}" option=r"${item.code} ${item.name}"
               style="width:200px;" items=disciplines empty="..." required="true"/]
    [@b.textfield label="经费" name="project.funds" required="true"  value=(project.funds)! comment="元" check='match("integer")'/]
    [@b.field label="指导老师"]
      <select id="instructorId" multiple="true" name="instructorId" style="width:400px;">
        [#list (project.instructors)! as i]
        <option value='${i.id}' selected>${i.user.name}(${i.user.code})</option>
        [/#list]
      </select>
    [/@]
    [@b.field label="其他成员"]
      <select id="studentId" multiple="true" name="studentId" style="width:400px;">
        [#list (project.members)! as m]
        [#if m.id!=(project.manager.id)!0]
        <option value='${m.std.id}' selected>${m.std.user.name}(${m.std.user.code})</option>
        [/#if]
        [/#list]
      </select>
    [/@]
    [@b.textarea name="intro.innovation" label="创新点和难点" value=(project.intro.innovation)! required="true" rows="5" cols="100" maxlength="200" comment="100字以内"/]
    [@b.textarea name="intro.product" label="预期成果" value=(project.intro.product)! required="true" rows="5" cols="100" maxlength="200" comment="100字以内"/]
    [@b.textfield name="project.remark" label="备注" value="${project.remark!}"  maxlength="100"/]
    [@b.formfoot]
      [#if project.manager??]
      <input type="hidden" name="manager.id" value="${project.manager.id}"/>
      [/#if]
      [#if project.intro??]
      <input type="hidden" name="intro.id" value="${project.intro.id}"/>
      [/#if]
      <input type="hidden" name="project.batch.id" value="${project.batch.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
[/@]

<script>
  jQuery("#instructorId").ajaxchosen(
    {
        method: 'GET',
        url:  "${b.url('!teacher?pageNo=1&pageSize=10')}"
    }
    , function (data) {
        var items = {};
        var dataObj = eval("(" + data + ")");
        jQuery.each(dataObj.teachers, function (i, teacher) {
            items[teacher.id] = teacher.name + "(" + teacher.code + ")";
        });
        return items;
    },
    {width:"400px"}
  );

    jQuery("#studentId").ajaxchosen(
    {
        method: 'GET',
        url:  "${b.url('!student?pageNo=1&pageSize=10')}"
    }
    , function (data) {
        var items = {};
        var dataObj = eval("(" + data + ")");
        jQuery.each(dataObj.students, function (i, student) {
            items[student.id] = student.name + "(" + student.code + ")";
        });
        return items;
    },
    {width:"400px"}
  );
</script>
[@b.foot/]
