package com.zero.milvus.common.imagesearch;

import org.deeplearning4j.zoo.model.ResNet50;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FeatureExtractor {

    private final ComputationGraph model;

	public FeatureExtractor() throws IOException {
		try {
			ZooModel<ComputationGraph> zooModel = ResNet50.builder().build();
			model = (ComputationGraph) zooModel.initPretrained();
		} catch (Exception e) {
			throw new IOException("Failed to initialize the pre-trained model: " + e.getMessage(), e);
		}
	}

    public INDArray extractFeatures(File imageFile) throws IOException {
        // todo 未能找到NativeImageLoader这个类 需要手工处理
//        import org.datavec.image.loader.NativeImageLoader;
//        NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
//        INDArray image = loader.asMatrix(imageFile);

        // 创建 INDArray image 不使用 NativeImageLoader
        INDArray image = createINDArrayFromImage(imageFile.getAbsolutePath());
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        scaler.transform(image);

        return model.outputSingle(image);
    }

    public static INDArray createINDArrayFromImage(String imagePath) throws IOException {
        // 读取图像文件
        BufferedImage bufferedImage = ImageIO.read(new File(imagePath));

        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        // 假设图像是 RGB 格式
        int channels = 3;

        // 创建存储像素数据的数组
        float[] imageData = new float[height * width * channels];
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bufferedImage.getRGB(x, y);

                // 提取 RGB 分量
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // 将 RGB 分量写入数组
                // 标准化到 [0, 1]
                imageData[index++] = red / 255.0f;
                imageData[index++] = green / 255.0f;
                imageData[index++] = blue / 255.0f;
            }
        }

        // 将数组转换为 INDArray
        return Nd4j.create(imageData, new int[]{1, channels, height, width});
    }

}