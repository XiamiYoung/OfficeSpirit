e:
cd E:\workspace\JLearner\resources
del resources_cn.properties
native2ascii -encoding utf-8 resources_cn_origin.properties resources_cn.properties

del resources_en.properties
native2ascii -encoding utf-8 resources_en_origin.properties resources_en.properties

del resources_jp.properties
native2ascii -encoding utf-8 resources_jp_origin.properties resources_jp.properties