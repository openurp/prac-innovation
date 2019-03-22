[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改课堂活动"]bar.addBack();[/@]
  <script type="text/javascript" crossorigin="anonymous" src="${base}/static/js/ajax-chosen.js"></script>
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(lesson)]
    [@b.textfield name="lesson.crn" label="序号" value="${lesson.crn!}" required="true" maxlength="20" style="width:200px;"/]
    [@b.datepicker label="日期" required="true" id="lesson.date" name="lesson.date"
    value="${(lesson.date?string('yyyy-MM-dd'))?default('')}" style="width:200px"  format="yyyy-MM-dd" /]
    [@b.startend label="时间"
      name="lesson.beginAt,lesson.endAt" required="true,true"
      start=(lesson.beginAt)! end=(lesson.endAt)! format="HH:mm" style="width:200px"/]
    [@b.textfield name="lesson.subject" label="活动名称" value="${lesson.subject!}" required="true" maxlength="100" style="width:200px;"/]
    [@b.select name="lesson.teachDepart.id" label="开课院系" value="${(lesson.teachDepart.id)!}" style="width:200px;" items=departments empty="..." required="true"/]
    [@b.field label="教室"]
      <select id="roomId" name="lesson.room.id" style="width:200px;">
        <option value='lesson.room.id' selected>${(lesson.room.name)!}</option>
      </select>
    [/@]
    [@b.textfield name="lesson.location" label="地点" value="${lesson.location!}" maxlength="100" style="width:200px;"/]
    [@b.textfield name="lesson.teachers" label="组织教师" value="${lesson.teachers!}" required="true" maxlength="500" style="width:200px;"/]
    [@b.textfield name="lesson.capacity" label="最大容量" value="${lesson.capacity!}" required="true" maxlength="20" style="width:200px;"/]
    [@b.textfield name="lesson.actual" label="实际学生" value="${lesson.actual!}" required="true" maxlength="20" style="width:200px;"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]

<script>
  jQuery("#roomId").ajaxChosen(
    {
        method: 'GET',
        url:  "${b.url('!room?pageNo=1&pageSize=10')}"
    }
    , function (data) {
        var items = {};
        var dataObj = eval("(" + data + ")");
        jQuery.each(dataObj.rooms, function (i, room) {
            items[room.id] = room.name ;
        });
        return items;
    },
    {width:"400px"}
  );
</script>
[@b.foot/]
