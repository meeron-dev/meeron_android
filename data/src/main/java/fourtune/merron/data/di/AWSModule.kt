package fourtune.merron.data.di

import android.content.Context
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AWSModule {
    @Provides
    @Singleton
    fun provideAWSCredentialsProvider(@ApplicationContext context: Context): AWSCredentialsProvider =
        CognitoCachingCredentialsProvider(
            context,
            IdentityPoolId, // 자격 증명 풀 ID
            Regions.AP_NORTHEAST_2, // 리전
        )

    @Provides
    @Singleton
    fun provideS3Client(awsCredentialsProvider: AWSCredentialsProvider): AmazonS3Client =
        AmazonS3Client(awsCredentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2))

    @Provides
    @Singleton
    fun provideTransferUtility(
        @ApplicationContext context: Context,
        s3Client: AmazonS3Client
    ): TransferUtility =
        TransferUtility
            .builder()
            .s3Client(s3Client)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .defaultBucket(Bucket)
            .context(context)
            .build()

    companion object {
        private const val IdentityPoolId = "ap-northeast-2:0f2f8135-ff74-4428-89bc-ec1433171376"
        private const val Bucket = "meeron-bucket"
    }
}