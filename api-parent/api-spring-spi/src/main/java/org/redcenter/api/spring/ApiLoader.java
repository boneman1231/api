package org.redcenter.api.spring;

import java.util.ServiceLoader;

import org.redcenter.api.spi.IApiClass;

public class ApiLoader {
	private static ServiceLoader<IApiClass> serviceLoader = ServiceLoader
			.load(IApiClass.class);

	public static void main(String[] args) {
		for (IApiClass service : serviceLoader) {
			System.out.println(service.getClass().getName());
		}
		System.out.println();
	}
}
