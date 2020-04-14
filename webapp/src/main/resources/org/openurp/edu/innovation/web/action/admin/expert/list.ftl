[#ftl]
[@b.head/]
[@b.grid items=experts var="expert"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.export")}",action.exportData("code:账户,name:姓名,password:密码,beginOn:有效期起始于,endOn:有效期结束(含)",null,'fileName=专家账户信息'));
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("设置开始日期",setBeginDate());
    bar.addItem("设置结束日期",setEndDate());
    bar.addItem("生成随机密码",action.multi("genPassword"));
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
    function NamedFunction(name,func,objectCount){
        this.name=name;
        this.func=func;
        this.objectCount=(null==objectCount)?'ge0':objectCount;
    }
    function setBeginDate(){
      return new NamedFunction('updateBeginOn',function(){
        try {
          var form = action.getForm();
          var beginOn = prompt("请输入结束日期,按照格式(yyyy-MM-dd)");
          if(beginOn){
            bg.form.addHiddens(form, "&beginOn="+beginOn);
            action.submitIdAction('updateBeginOn', true, "确定更新开始日期?",true);
          }
        }catch(e){
          bg.alert(e)
        }
      },bg.ui.grid.enableDynaBar?'ge1':'ge0');
    }
    function setEndDate(){
      return new NamedFunction('updateEndOn',function(){
        try {
          var form = action.getForm();
          var endOn= prompt("请输入结束日期,按照格式(yyyy-MM-dd)");
          if(endOn){
            bg.form.addHiddens(form, "&endOn="+endOn);
            action.submitIdAction('updateEndOn', true, "确定更新结束日期?",true);
          }
        }catch(e){
          bg.alert(e)
        }
      },bg.ui.grid.enableDynaBar?'ge1':'ge0');
    }
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="code" title="账户"/]
    [@b.col width="15%" property="name" title="姓名"][@b.a href="!info?id=${expert.id}"]${expert.name}[/@][/@]
    [@b.col width="15%" property="password" title="密码"]<span title="${expert.password}">***</span>[/@]
    [@b.col width="20%" property="beginOn" title="有效期"]
      ${expert.beginOn?string('yyyy-MM-dd')}~${expert.endOn?string('yyyy-MM-dd')}
    [/@]
    [@b.col width="45%"  property="intro" title="介绍"/]
  [/@]
[/@]
[@b.foot/]
