package aries.kim.gulimall.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import kim.aries.gulimall.product.GulimallProductApplication;
import kim.aries.gulimall.product.entity.BrandEntity;
import kim.aries.gulimall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GulimallProductApplication.class)
public class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("小米");
//        //保存
//        brandEntity.setName("小米");
//        brandService.save(brandEntity);
//        System.out.println("保存成功...");

/*        //更新
        brandService.updateById(brandEntity);*/

        //查询
        List<BrandEntity> result = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        result.forEach((e) -> {
            System.out.println(e);
        });
    }

}
