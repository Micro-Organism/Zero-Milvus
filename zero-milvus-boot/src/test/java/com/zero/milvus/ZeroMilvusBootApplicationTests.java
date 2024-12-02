package com.zero.milvus;

import com.zero.milvus.common.imagesearch.FeatureExtractor;
import com.zero.milvus.common.imagesearch.ImageSearcher;
import com.zero.milvus.common.imagesearch.MilvusManager;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class ZeroMilvusBootApplicationTests {

    @Test
    void contextLoads() throws IOException {
        FeatureExtractor extractor = new FeatureExtractor();
        MilvusManager milvusManager = new MilvusManager();
        ImageSearcher searcher = new ImageSearcher();

        // 创建Milvus集合
        milvusManager.createCollection();

        // 设置路径为项目执行路径
        String dirPath = System.getProperty("user.dir");
        // 指定到resources目录下的images文件夹
        String imgPath = dirPath + "/src/main/resources/images";

        // 假设有一个图像文件列表
        File[] imageFiles = new File(imgPath).listFiles();
        if (imageFiles != null) {
            for (int i = 0; i < imageFiles.length; i++) {
                INDArray features = extractor.extractFeatures(imageFiles[i]);
                milvusManager.insertData(i, features);
            }
        }
        milvusManager.flush();
        milvusManager.buildindex();


        // 查询图像
        File queryImage = new File(imgPath + "/cat001.jpg");
        INDArray queryFeatures = extractor.extractFeatures(queryImage);
        searcher.search(queryFeatures);
    }

}
