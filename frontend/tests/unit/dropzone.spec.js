import { shallowMount } from '@vue/test-utils';
import Main from '@/view/Main';
import ProgressBar from '@/components/ProgressBar';
import VueDropzone from 'vue2-dropzone';
import axios from 'axios';
import UploadApi from '@/api/upload.api.js';

describe('Dropzone Test', () => {
  let wrapper = shallowMount(Main);

  beforeEach(() => {
    wrapper.vm.clearCount();
  });

  it('renders dropzone', () => {
    expect(wrapper.exists()).toBeTruthy();
  });

  it('VueDropzone 이 존재함', () => {
    expect(wrapper.findComponent(ProgressBar).exists()).toBeTruthy();
    expect(wrapper.findComponent(VueDropzone).exists()).toBeTruthy();
  });

  it('updateCount를 테스트', () => {
    wrapper.vm.updateCount({ successCount: 10, failCount: 10 });
    expect(wrapper.vm.$data.progress.success).toEqual(10);
    expect(wrapper.vm.$data.progress.fail).toEqual(10);
  });

  it('clearCount를 테스트', () => {
    wrapper.vm.clearCount();
    expect(wrapper.vm.$data.progress.success).toEqual(0);
    expect(wrapper.vm.$data.progress.fail).toEqual(0);
  });

  it('polling times Test', (done) => {
    UploadApi.getUploadStatus = jest.fn().mockResolvedValue({
      successCount: 10,
      failCount: 10,
    });
    const spyOn = jest.spyOn(UploadApi, 'getUploadStatus');
    wrapper.vm.startPollingTimer();

    setTimeout(() => {
      expect(spyOn).toBeCalledTimes(2);
      done();
    }, 3000);
  });

  it('start and stop polling Test', () => {
    UploadApi.getUploadStatus = jest.fn().mockResolvedValue({
      successCount: 10,
      failCount: 10,
    });

    wrapper.vm.startPollingTimer();
    setTimeout((done) => {
      expect(wrapper.vm.$data.progress.success).toEqual(10);
      expect(wrapper.vm.$data.progress.fail).toEqual(10);
      !expect(wrapper.vm.$data.interval).toEqual(-1);
      done();
    }, 1000);

    wrapper.vm.stopPollingTimer();
    expect(wrapper.vm.$data.interval).toEqual(-1);
  });

  it('file upload Test', () => {
    axios.get = jest.fn().mockResolvedValue();
    VueDropzone.processQueue = jest.fn();
  });
});
