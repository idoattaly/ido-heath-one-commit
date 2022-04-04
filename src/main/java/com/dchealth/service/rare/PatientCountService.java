package com.dchealth.service.rare;

import com.dchealth.VO.*;
import com.dchealth.facade.common.BaseFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/30.
 */
@Produces("application/json")
@Path("patcount")
@Controller
public class PatientCountService {
    @Autowired
    private BaseFacade baseFacade;

    private String[] ranges = {"0-10","10-20","20-30","30-40","40-50","50-60","60"};

    /**
     * 根据疾病编码查询疾病男女比例信息及地域分布信息
     * @param dcode
     * @return
     */
    @GET
    @Path("get-pat-count")
    public PatCountData getPatCountData(@QueryParam("dcode")String dcode){
        SexTotal sexTotal = new SexTotal();
        List<SexRangeData> sexRangeDataList = new ArrayList<>();
        String sql = "select p.sx,count(p.id) from yun_folder f,yun_patient p where f.patient_id = p.id and p.sx in ('男','女')" +
                     " and f.diagnosis_code='"+dcode+"' and p.age is not null  GROUP BY p.sx";
        List list = baseFacade.createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                Object[] params = (Object[])list.get(i);
                if("男".equals(params[0].toString())){
                    sexTotal.setFtotal(Integer.valueOf(params[1].toString()));
                }else{
                    sexTotal.setMtotal(Integer.valueOf(params[1].toString()));
                }
            }
        }
        for(String range:ranges){
            SexRangeData sexRangeData = getSexRangeData(range,dcode);
            sexRangeDataList.add(sexRangeData);
        }
        List<RegionPatData> regionPatDatas = getRegionPatDatas(dcode);
        PatCountData sexData = new PatCountData(sexTotal,regionPatDatas,sexRangeDataList);
        return sexData;
    }

    public List<RegionPatData> getRegionPatDatas(String dcode){
        List<RegionPatData> regionPatDataList = new ArrayList<>();
        Map<String,Map> map = new HashMap<String,Map>();
        String sql = "select (select nation from yun_users where id = p.doctor_id) as nation," +
                     "(select hospital_name from yun_users where id = p.doctor_id) as hospitalName," +
                     "count(p.id) from yun_folder f,yun_patient p where f.patient_id = p.id and f.diagnosis_code = '"+dcode+"'" +
                     "group by p.doctor_id";
        List list = baseFacade.createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()) {
            int size = list.size();
            for(int i=0;i<size;i++){
                Object[] params = (Object[])list.get(i);
                String nation = params[0]==null?"":params[0].toString();
                String hospitalName = params[1]==null?"":params[1].toString();
                if(!"".equals(nation)&&!"".equals(hospitalName)){
                    int count = Integer.valueOf(params[2].toString());
                    if(map.get(nation)!=null){
                        Map patMap = map.get(nation);
                        if(patMap.containsKey(hospitalName)){
                            int lastCount = Integer.valueOf(patMap.get(hospitalName).toString());
                            int nowCount = lastCount+count;
                            patMap.put(hospitalName,nowCount);
                        }else{
                            patMap.put(hospitalName,count);
                        }
                    }else{
                        Map patMap = new HashMap();
                        patMap.put(hospitalName,count);
                        map.put(nation,patMap);
                    }
                }
            }
        }
        for(String key:map.keySet()){
            RegionPatData regionPatData = new RegionPatData();
            List<HospitalPatData> hospitalPatDataList = new ArrayList<>();
            regionPatData.setRegion(key);
            Map patcountMap = map.get(key);
            for(Object patkey:patcountMap.keySet()){
                HospitalPatData hospitalPatData = new HospitalPatData();
                String hospital = patkey.toString();
                int count =Integer.valueOf(patcountMap.get(hospital).toString());
                hospitalPatData.setHospitalName(hospital);
                hospitalPatData.setPatCount(count);
                hospitalPatDataList.add(hospitalPatData);
            }
            regionPatData.setHospitalPatDatas(hospitalPatDataList);
            regionPatDataList.add(regionPatData);
        }
        return regionPatDataList;
    }
    /**
     * 根据年龄范围查询数据
     * @param range
     * @return
     */
    private SexRangeData getSexRangeData(String range,String dcode) {
        SexRangeData sexRangeData = new SexRangeData();
        String sql = "select p.sx,count(p.id) from yun_folder f,yun_patient p where f.patient_id = p.id and p.sx in ('男','女') " +
                     "and f.diagnosis_code='"+dcode+"'";
        if(range.contains("-")){
            sexRangeData.setTitle(range+"岁");
            String[] range_nums = range.split("-");
            sql+= " and p.age>="+Integer.valueOf(range_nums[0])+" and p.age<"+Integer.valueOf(range_nums[1]);
        }else{
            sexRangeData.setTitle(range+"岁以上");
            sql+= " and p.age>="+Integer.valueOf(range);
        }
        sql+= " GROUP BY p.sx";
        List list = baseFacade.createNativeQuery(sql).getResultList();
        if(list!=null && !list.isEmpty()){
            int size = list.size();
            for(int i=0;i<size;i++){
                Object[] params = (Object[])list.get(i);
                if("男".equals(params[0].toString())){
                    sexRangeData.setFvalue(Integer.valueOf(params[1].toString()));
                }else{
                    sexRangeData.setMvalue(Integer.valueOf(params[1].toString()));
                }
            }
        }
        return sexRangeData;
    }

}
