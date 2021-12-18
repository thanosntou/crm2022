package com.ntouzidis.crm2022.module.email;

import java.util.List;

public record EmailForm(List<Long> to, boolean all, String subject, String content) {
}
