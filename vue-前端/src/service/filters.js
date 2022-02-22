// 毫秒值转日期字符串
export const dateFormat = (time)=>{
    let date = new Date(time);
    // 分别获取 年 月 日 时 分 秒
    let year = date.getFullYear();
    let month = (date.getMonth() + 1 + "").padStart(2, "0");
    let day = (date.getDate() + "").padStart(2, "0");
    let hours = (date.getHours() + "").padStart(2, "0");
    let minutes = (date.getMinutes() + "").padStart(2, "0");
    let seconds = (date.getSeconds() + "").padStart(2, "0");
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};
