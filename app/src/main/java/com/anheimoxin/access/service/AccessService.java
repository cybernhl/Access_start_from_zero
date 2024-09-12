package com.anheimoxin.access.service;

import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * 操作类，在这里实现具体逻辑
 */
public class AccessService extends BaseService {

    private String appPackageName = "com.huawei.calculator";
    private boolean refresh = true; // 控制在未处理完逻辑前不要进入逻辑空间
    private int progress = 0; // 自动化进度

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();
        Log.d("packageName", packageName);
        if (!packageName.equals(appPackageName)) {
            // 如果活动APP不是目标APP则不响应
            return;
        }
        int eventType = event.getEventType();

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:// 捕获窗口内容改变事件
                if (packageName.equals(appPackageName)) {
                    if (refresh) {
                        refresh = false;
                        autoCalculator();
                        refresh = true;
                    }
                }
                break;
            default:
                break;
        }
    }

    private void autoCalculator() {
        // progress自动化进度，防止顺序出错
        // 步骤1：点击 1
        if (progress == 0) {
            // 直接通过文本查找数字 1
            AccessibilityNodeInfo nodeOne = findViewByText("1");
            if (nodeOne != null) {
                performViewClick(nodeOne);
                progress++;
                sleep(500);
            }
        }

        // 步骤2：点击 +
        if (progress == 1) {
            // 有些view是没有text的，就可以通过ID、类名等属性来获取
            AccessibilityNodeInfo nodeAdd = findViewByID("com.huawei.calculator:id/op_add");
            if (nodeAdd != null) {
                performViewClick(nodeAdd);
                progress++;
                sleep(500);
            }
        }

        // 步骤3：点击 2
        if (progress == 2) {
            // 通过文本查找所有的 2，并点击
            List<AccessibilityNodeInfo> nodeOneList = findNodesByText("2");
            if (nodeOneList != null && nodeOneList.size() != 0) {
                for (int i = 0; i < nodeOneList.size(); i++) {
                    AccessibilityNodeInfo node = nodeOneList.get(i);
                    if (node != null) {
                        // 用于获取控件的矩形信息：坐标位置和宽高
                        Rect rect = new Rect();
                        node.getBoundsInScreen(rect);

                        int moveToX = (rect.left + rect.right) / 2;
                        int moveToY = (rect.top + rect.bottom) / 2;
                        int lineToX = (rect.left + rect.right) / 2;
                        int lineToY = (rect.top + rect.bottom) / 2;

                        // 有些View是不能点击，这时候可以用手势来处理
                        gesture(moveToX, moveToY, lineToX, lineToY, 100L, 400L);
                        progress++;
                        sleep(500);
                        break;
                    }
                }
            }
        }
        // 步骤4：点击 +
        if (progress == 3) {
            AccessibilityNodeInfo nodeAdd = findViewByID("com.huawei.calculator:id/op_add");
            if (nodeAdd != null) {
                performViewClick(nodeAdd);
                progress++;
                sleep(500);
            }
        }

        // 步骤5：点击 3
        if (progress == 4) {
            // getRootInActiveWindow返回整个view的root节点，深度优先遍历查找所有的3，并点击
            clickNodesByText("3", getRootInActiveWindow());
            progress++;
            sleep(500);
        }

        // 步骤6：点击 =
        if (progress == 5) {
            AccessibilityNodeInfo nodeEq = findViewByID("com.huawei.calculator:id/eq");
            if (nodeEq != null) {
                performViewClick(nodeEq);
                progress = 0;
                sleep(500);
            }
        }
        // 更多的操作请看BaseService，或者自行百度
    }
}
