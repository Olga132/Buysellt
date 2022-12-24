<#import "layuot/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Купить или продать все что угодно!</h1>
    <hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <select id="searchCity" name="searchCity" <#if searchCity??>value="${searchCity}"</#if>
                        class="form-control">
                    <option value="">Город не выбран</option>
                    <option value="Москва">Москва</option>
                    <option value="Санкт-Петербург">Санкт-Петербург</option>
                    <option value="Екатеринбург">Екатеринбург</option>
                    <option value="Казань">Казань</option>
                    <option value="Пермь">Пермь</option>
                </select>
            </div>
            <div class="form-group col-md-8">
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
                       id="searchWord" placeholder="Поиск по объявлениям">
            </div>
            <div class="form-group col-md-2">
            <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
            </div>
        </div>
    </form>
    <hr>
    <div class="row">
            <#list products as product>
                <div class="col-3">
                <a href="/product/${product.id}" class="product-link">
                    <div class="card mb-4 product" style="width: 15rem; border-color: dimgray">
                        <img src="/images/${product.previewImageId}" class="card-img-top my-image"
                             style="width: 80%; display: block; margin-top: 5px; margin-left: auto; margin-right: auto">
                        <div class="card-body">
                            <h5 class="card-title">${product.title}</h5>
                            <p class="card-text">${product.price} ₽</p>
                        </div>
                        <div class="card-footer">
                            ${product.city}
                        </div>
                    </div>
                </a>
                </div>
            <#else>
                <h1 style="text-align: center">Не найдено</h1>
            </#list>
        </div>
</@t.template>