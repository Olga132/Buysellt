<#import "layuot/template.ftlh" as t>
<@t.template user>
    <div class="text-center">
        <#list images as image>
            <a href="/images/${image.id}">
                <img class="product" style="height: 250px" src="/images/${image.id}">
            </a>
        </#list>
    </div>
    <hr>
    <p><h1 style="text-align: center">${product.title}</h1></p>
    <p><h1 style="text-align: center">Цена ${product.price} ₽</h1></p>
    <p style="text-align: center">Номер телефона продавца <b>${authorProduct.name}</b> :
        <b>${authorProduct.phoneNumber}</b></p>
    <p style="text-align: center">${product.description}</p>

<#--    Сообщение продавцу-->
    <hr>
    <a class="btn btn-dark mt-3 mb-3" style="text-align: center; width: 100%"
       data-toggle="collapse" href="#collapseExample" role="button"
       aria-expanded="true" aria-controls="collapseExample">
        Написать сообщение продавцу
    </a>
    <div class="collapse mb-3" id="collapseExample" style="">
        <form action="/product/${product.id}" method="post">
            <div class="form-group">
                <label for="email">Электронная почта</label>
                <input type="email" class="form-control" id="email" required name="email">
            </div>
            <div class="form-group">
                <label for="phoneNumber">Номер телефона</label>
                <input type="text" class="form-control" id="phoneNumber" required name="phoneNumber">
            </div>
            <div class="form-group">
                <label for="name">Имя пользователя</label>
                <input type="text" class="form-control" required id="name" name="name">
            </div>
            <div class="form-group">
                <label for="description">Сообщение</label>
                <textarea required class="form-control" id="message" name="message"></textarea>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" style="width: 100%" class="btn btn-dark">Отправить сообщение</button>
        </form>
    </div>
<#--    Сообщение продавцу-->
    <hr>
    <p style="text-align: center">
        <a href="/user/${authorProduct.id}">
            <img src="/static/images/avatar.png" alt="mdo" width="32" height="32"
                 class="rounded-circle"> ${authorProduct.name}
        </a>
    </p>

</@t.template>