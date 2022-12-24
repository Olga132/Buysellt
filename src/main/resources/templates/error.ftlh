<#import "layuot/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Ошибка</h1>
    <hr>
    <div class="wrapper">
        <div class="content">
            <h2>Упс... Что-то пошло не так!</h2>
            <h2>${messageError?if_exists}</h2>


            <a href="/">
                <button type="submit" class="btn btn-dark">
                    Возврат на главную
                </button>
            </a>

        </div>
    </div>
</@t.template>