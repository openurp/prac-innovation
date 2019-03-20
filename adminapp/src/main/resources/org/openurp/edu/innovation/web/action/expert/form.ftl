[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改评审专家"]bar.addBack();[/@]
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(expert)]
    [@b.textfield name="expert.name" label="名称" value="${expert.name!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.textfield name="expert.code" label="账户" value="${expert.code!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.textfield name="expert.password" label="密码" value="${expert.password!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.textarea name="expert.intro" label="介绍" value="${expert.intro!}" required="true" maxlength="500" style="width:200px;"/]
    [@b.select name="expert.discipline.id" label="学科" value="${(expert.discipline.id)!}" style="width:200px;" items=disciplines empty="..." required="true"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]