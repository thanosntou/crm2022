package com.ntouzidis.crm2022.module.email;

import com.ntouzidis.crm2022.module.common.enumeration.BusinessType;
import lombok.NonNull;

import java.util.List;

public record EmailForm(List<Long> to, boolean all,
                        String subject,
                        @NonNull String content,
                        @NonNull BusinessType businessType) {
}
