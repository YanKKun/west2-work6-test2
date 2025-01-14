--KEYS对应 限流key
local key = KEYS[1]
--第一个可变参数 maxTimes-限流次数
local count = tonumber(ARGV[1])
--第二个可变参数 second-限流时间
local time = tonumber(ARGV[2])
--当前时间窗内这个接口可以访问多少次
local current = redis.call('get', key)
--如果当前可以访问次数大于限流次数则直接返回当当前访问次数
if current and tonumber(current) > count then
    return tonumber(current)
end
--如果当前可以访问次数不大于限流次数则直接返回当前可访问次数自增
current = redis.call('incr', key)
--如果是第一次访问，保存key设置一个过期时间。
if tonumber(current) == 1 then

    redis.call('expire', key, time)
end
--返回当前访问次数
return tonumber(current)
