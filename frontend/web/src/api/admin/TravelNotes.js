import axios from "~/axios";


/** 1
 * 添加游记
 * @param {object} params 游记添加实体
 * @param {string} params.title 
 * @param {string} params.content 
 * @param {string} params.mediaRes 
 * @returns
 */
// export function addMTravelNotes(params) {
//   return axios.post(`/service/mTravelNotes/addMTravelNotes`, params);
// }
export function addMTravelNotes(params) {
  return axios.post(`/service/mTravelNotes/addMTravelNotes`, params)
    .then(response => {
      console.log('添加游记响应数据:', response.data);
      return response;
    })
    .catch(error => {
      console.error('添加游记失败:', error);
      throw error;
    });
}



/** 2
 * 获取游记详情
 * @param {string} id 
  * @returns
 */
export function getTravelNotesInfo(id) {
  return axios.get(`/service/mTravelNotes/getInfo/${id}`);
}

/** 3
 * 分页查询游记
 * @param {object} params 游记查询实体
 * @param {number} params.pageNum 
 * @param {number} params.pageSize 
 * @param {string} params.title 
 * @param {string} params.content 
 * @param {number} params.status 
 * @param {number} params.isRecommend 
 * @param {number} params.createId 
 * @param {number} params.updateId 
 * @param {object} params.startTime 
 * @param {object} params.endTime 
 * @returns
 */
export function pageTravelNotes(params) {
  return axios.post(`/service/mTravelNotes/page`, params);
}


/** 4
 * 删除游记
 * @param {string} id 
  * @returns
 */
export function removeTravelNotes(id) {
  return axios.delete(`/service/mTravelNotes/remove/${id}`);
}

/** 5
 * 更新游记
 * @param {object} params 游记更新实体
 * @param {number} params.id 
 * @param {string} params.title 
 * @param {string} params.content 
 * @param {string} params.mediaRes 
 * @param {number} params.status 
 * @param {object} params.createTime 
 * @returns
 */
export function updateTravelNotes(params) {
  return axios.put(`/service/mTravelNotes/updateMTravelNotes`, params);
}