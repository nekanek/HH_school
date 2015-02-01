-- 1. Получить список organization_form, использующихся в таблице employer отсортированные по убыванию

set search_path = 'hhschool';
SELECT DISTINCT organization_form FROM employer 
    ORDER BY organization_form DESC;


-- 2. Получить количество работодателей каждой из organization_form, 
-- отбросив все organization_form для которых найдено менее 3 записей. 
-- Результат отсортировать по убыванию количества найденных записей
-- (0)

-- не уверена, нужно ли выводить результаты с пустым полем organization_form 
set search_path = 'hhschool';
SELECT COUNT(employer_id), organization_form FROM employer 
    GROUP BY organization_form  
    HAVING COUNT(employer_id) > 2
    ORDER BY COUNT(employer_id) DESC
    -- AND organization_form IS NOT NULL
;


-- 3. Получить список переводов (таблица translation, каждый перевод характеризуется уникальным набором полей name, lang, site_id), 
-- для которых есть русская версия (lang =‘RU’), 
-- но нет украинской (lang = ‘UA’) и 
-- флаг ui установлен в true

SELECT * FROM translation
    WHERE ui=true AND 
          lang='RU' AND 
          CONCAT(name, site_id) NOT IN 
            (SELECT CONCAT(name, site_id) 
             FROM translation 
             WHERE lang='UA' )
;


-- 4. Для каждого user_id получить самую раннюю (по modification_time) запись в translation_history 
-- для которых ui = true и 
-- вывести только 10 записей самых ранних по modification_time 
-- отсортированных по возрастанию modification_time


-- группировка по user_id, без условия на несуществующее поле ui
-- пришлось перечислить поля вместо *, чтобы в результатах не было повторных колонок q.user_id и q.minTime 
set search_path = 'hhschool';
SELECT translation_history_id, modification_time, 
    t.user_id, site_id, lang, name, 
    old_value, new_value

    FROM translation_history t RIGHT JOIN 

        (SELECT user_id, MIN(modification_time) as minTime  
            FROM translation_history as h
            GROUP BY user_id
            ORDER BY minTime ASC
            LIMIT 10
        ) q

    ON t.user_id = q.user_id AND t.modification_time = q.minTime 
    ORDER BY modification_time ASC 
;


-- группировка по name - тут хоть есть результат
set search_path = 'hhschool';
SELECT translation_history_id, modification_time, 
    user_id, site_id, lang, t.name, 
    old_value, new_value

    FROM translation_history t RIGHT JOIN 

        (SELECT name, MIN(modification_time) as minTime  
            FROM translation_history as h
            GROUP BY name
            ORDER BY minTime ASC
            LIMIT 10
        ) q

    ON t.name = q.name AND t.modification_time = q.minTime 
    ORDER BY modification_time ASC 
;

-- с условием на несуществующий ui, группировка по user_id 
set search_path = 'hhschool';
SELECT translation_history_id, modification_time, 
    t.user_id, site_id, lang, name, 
    old_value, new_value, t.ui

    FROM translation_history t RIGHT JOIN 

        (SELECT user_id, MIN(modification_time) as minTime
            FROM translation_history as h
            GROUP BY user_id, ui
            HAVING ui=true 
            ORDER BY minTime ASC
            LIMIT 10
        ) q

    ON t.user_id = q.user_id AND t.modification_time = q.minTime 
    ORDER BY modification_time ASC 
;
