package uk.gov.justice.hmpps.datacompliance.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Slf4j
@EnableJms
@Configuration
@ConditionalOnExpression("{'aws', 'localstack'}.contains('${inbound.referral.sqs.provider}')")
public class InboundReferralQueueConfig {

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            @Qualifier("inboundReferralSqsClient") final AmazonSQS awsSqsClient,
            @Value("${inbound.referral.sqs.concurrency:1}") final String concurrency) {

        final var factory = new DefaultJmsListenerContainerFactory();

        factory.setConnectionFactory(new SQSConnectionFactory(new ProviderConfiguration(), awsSqsClient));
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency(concurrency);
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setErrorHandler(throwable -> log.error("JMS error occurred", throwable));

        return factory;
    }

    @Bean
    @ConditionalOnProperty(name = "inbound.referral.sqs.provider", havingValue = "aws")
    public AmazonSQS inboundReferralSqsClient(
            @Value("${inbound.referral.sqs.aws.access.key.id}") final String accessKey,
            @Value("${inbound.referral.sqs.aws.secret.access.key}") final String secretKey,
            @Value("${inbound.referral.sqs.region}") final String region) {

        log.info("Creating AWS inbound referral SQS client");

        var credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "inbound.referral.sqs.provider", havingValue = "aws")
    public AmazonSQS inboundReferralSqsDlqClient(
            @Value("${inbound.referral.sqs.dlq.aws.access.key.id}") final String accessKey,
            @Value("${inbound.referral.sqs.dlq.aws.secret.access.key}") final String secretKey,
            @Value("${inbound.referral.sqs.region}") final String region) {

        log.info("Creating AWS inbound referral SQS client for DLQ");

        var credentials = new BasicAWSCredentials(accessKey, secretKey);

        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    @Bean("inboundReferralSqsClient")
    @ConditionalOnProperty(name = "inbound.referral.sqs.provider", havingValue = "localstack")
    public AmazonSQS sqsClientLocalstack(
            @Value("${inbound.referral.sqs.endpoint.url}") final String serviceEndpoint,
            @Value("${inbound.referral.sqs.region}") final String region) {

        log.info("Creating Localstack inbound referral SQS client");

        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration(serviceEndpoint, region))
                .build();
    }

    @Bean("inboundReferralSqsDlqClient")
    @ConditionalOnProperty(name = "inbound.referral.sqs.provider", havingValue = "localstack")
    public AmazonSQS sqsDlqClientLocalstack(
            @Value("${inbound.referral.sqs.endpoint.url}") final String serviceEndpoint,
            @Value("${inbound.referral.sqs.region}") final String region) {

        log.info("Creating Localstack inbound referral SQS client for DLQ");

        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new EndpointConfiguration(serviceEndpoint, region))
                .build();
    }
}
