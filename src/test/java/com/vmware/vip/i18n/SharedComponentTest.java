/*
 * Copyright 2019 VMware, Inc.
 * SPDX-License-Identifier: EPL-2.0
 */
package com.vmware.vip.i18n;

import com.vmware.vipclient.i18n.I18nFactory;
import com.vmware.vipclient.i18n.VIPCfg;
import com.vmware.vipclient.i18n.base.cache.FormattingCache;
import com.vmware.vipclient.i18n.base.cache.MessageCache;
import com.vmware.vipclient.i18n.base.instances.TranslationMessage;
import com.vmware.vipclient.i18n.exceptions.VIPClientInitException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class SharedComponentTest extends BaseTestClass {
    TranslationMessage mainTranslation;
    TranslationMessage subTranslation;
    String mainProductName = "JavaclientTest";
    String subProductName = "JavaclientTest1";

    @Before
    public void init() {
        VIPCfg mainCfg = VIPCfg.getInstance();
        try {
            mainCfg.initialize("vipconfig");
        } catch (VIPClientInitException e) {
            e.printStackTrace();
        }
        mainCfg.initializeVIPService();
        if (mainCfg.getCacheManager() != null) mainCfg.getCacheManager().clearCache();
        mainCfg.createTranslationCache(MessageCache.class);
        mainCfg.createFormattingCache(FormattingCache.class);
        I18nFactory i18n = I18nFactory.getInstance(mainCfg);
        mainTranslation = (TranslationMessage) i18n.getMessageInstance(TranslationMessage.class);

        VIPCfg subCfg = VIPCfg.getSubInstance(subProductName);
        try {
            subCfg.initialize("vipconfig-child");
        } catch (VIPClientInitException e) {
            e.printStackTrace();
        }
        subTranslation = (TranslationMessage) i18n.getMessageInstance(TranslationMessage.class, subCfg);
    }

    @Test
    public void testGetSharedModuleTranslation() {
        Locale zhLocale = new Locale("zh", "Hans");
        String comp = "JAVA";
        String key = "table.host";
        String source = "Host";
        String trans1 = mainTranslation.getString(zhLocale, comp, key, source, "");
        logger.debug("pseudoTrans1: " + trans1);

        Locale zhLocale2 = new Locale("de", "");
        String comp2 = "JSP";
        String key2 = "table.head";
        String source2 = "VM";
        String trans2 = subTranslation.getString(zhLocale2, comp2, key2, source2, "");
        logger.debug("pseudoTrans1: " + trans2);
        Assert.assertTrue(VIPCfg.getInstance().getProductName().equals(mainProductName));
        Assert.assertTrue(subTranslation.getCfg().getProductName().equals(subProductName));

    }
}
