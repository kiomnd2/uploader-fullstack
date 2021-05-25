import { shallowMount, createLocalVue } from '@vue/test-utils';
import ProgressBar from '@/components/ProgressBar';
import BootstrapVue from 'bootstrap-vue';

const localVue = createLocalVue();

localVue.use(BootstrapVue);

describe('Dropzone.vue Test', () => {
  let failCount = 5;
  let successCount = 100;

  const wrapper = shallowMount(ProgressBar, {
    propsData: {
      progress: 25,
      successCount: successCount,
      failCount: failCount,
    },
    localVue,
  });

  it('render progressbar', () => {
    expect(wrapper.exists()).toBe(true);
  });

  it('maxCount = successCount + failCout * 100/ progress ', () => {
    expect(wrapper.vm.maxCount).toBe(420);
  });
});
