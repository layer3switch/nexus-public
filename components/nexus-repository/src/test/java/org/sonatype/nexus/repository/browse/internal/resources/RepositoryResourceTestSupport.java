/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.repository.browse.internal.resources;

import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.common.entity.EntityId;
import org.sonatype.nexus.common.entity.EntityMetadata;
import org.sonatype.nexus.repository.Format;
import org.sonatype.nexus.repository.Repository;
import org.sonatype.nexus.repository.browse.BrowseService;
import org.sonatype.nexus.repository.storage.Asset;
import org.sonatype.nexus.repository.storage.StorageFacet;
import org.sonatype.nexus.repository.storage.StorageTx;

import com.google.common.base.Supplier;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public abstract class RepositoryResourceTestSupport
    extends TestSupport
{
  @Mock
  RepositoryManagerRESTAdapter repositoryManagerRESTAdapter;

  @Mock
  BrowseService browseService;

  @Mock
  Format format;

  @Mock
  Repository mavenReleases;

  @Mock
  StorageFacet storageFacet;

  @Mock
  StorageTx storageTx;

  Supplier<StorageTx> storageTxSupplier;

  String mavenReleasesId = "maven-releases";

  @Before
  public void init() {
    configureMockedRepository(mavenReleases, mavenReleasesId, "http://localhost:8081/repository/maven-releases");

    when(format.toString()).thenReturn("maven2");
    when(format.getValue()).thenReturn("maven2");

    storageTxSupplier = () -> storageTx;
    when(storageFacet.txSupplier()).thenReturn(storageTxSupplier);

  }

  protected void configureMockedRepository(Repository repository,
                                           String name,
                                           String url)
  {
    when(repositoryManagerRESTAdapter.getRepository(name)).thenReturn(repository);
    when(repository.getUrl()).thenReturn(url);
    when(repository.getName()).thenReturn(name);
    when(repository.getFormat()).thenReturn(format);
    when(repository.facet(StorageFacet.class)).thenReturn(storageFacet);
  }

  Asset getMockedAsset(String name, String idValue) {
    AssetMocks assetMocks = new AssetMocks();
    MockitoAnnotations.initMocks(assetMocks);

    when(assetMocks.asset.name()).thenReturn(name);
    when(assetMocks.asset.getEntityMetadata()).thenReturn(assetMocks.assetEntityMetadata);

    when(assetMocks.assetEntityMetadata.getId()).thenReturn(assetMocks.assetEntityId);
    when(assetMocks.assetEntityId.getValue()).thenReturn(idValue);

    return assetMocks.asset;
  }

  class AssetMocks
  {
    @Mock
    Asset asset;
    
    @Mock
    EntityMetadata assetEntityMetadata;

    @Mock
    EntityId assetEntityId;
  }
}
