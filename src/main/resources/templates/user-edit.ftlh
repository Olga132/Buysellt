<#import "layuot/template.ftlh" as t>
<@t.template user>
<h1 style="text-align: center">Редактирование пользователя ${userChange.name}</h1><hr>
<form action="/admin/user/edit" method="post">
    <#list roles as role>
        <div>
            <label><input type="checkbox" name="${role}" ${userChange.roles?seq_contains(role)?string("checked", "")}>${role}</label>
        </div>
    </#list>
    <input type="hidden" value="${userChange.id}" name="userId">
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <button type="submit" class="btn btn-dark">Сохранить</button>
</form>

</@t.template>