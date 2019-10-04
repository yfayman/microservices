package com.yan.photoappdiscovery.api.users.data;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yan.photoappdiscovery.api.users.model.AlbumResponseModel;

import feign.hystrix.FallbackFactory;

@Component
public class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {


	@Override
	public AlbumsServiceClient create(Throwable cause) {
		return new AlbumsServiceClientFallback(cause);
	}

}

class AlbumsServiceClientFallback implements AlbumsServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(AlbumsServiceClientFallback.class);
	
	private Throwable cause;
	
	public AlbumsServiceClientFallback(Throwable cause) {
		this.cause = cause;
	}
	
	@Override
	public List<AlbumResponseModel> getAlbums(String id) {
		
		
		
		return Collections.emptyList();
	}
	
}
