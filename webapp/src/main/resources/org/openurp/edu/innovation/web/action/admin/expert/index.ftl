[#ftl]
[@b.head/]
[@b.toolbar title="评审专家管理"/]
<div class="search-container">
    <div class="search-panel" >
    [@b.form name="expertSearchForm" action="!search" target="expertlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="expert.code;账户"/]
      [@b.textfields names="expert.name;姓名"/]
      <input type="hidden" name="orderBy" value="name desc"/>
    [/@]
    </div>
    <div class="search-list">
    [@b.div id="expertlist" href="!search?orderBy=name desc"/]
    </div>
  </div>
[@b.foot/]
